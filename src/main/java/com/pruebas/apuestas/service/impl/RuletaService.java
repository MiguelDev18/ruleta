package com.pruebas.apuestas.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pruebas.apuestas.dto.ApuestaRequestDTO;
import com.pruebas.apuestas.dto.ApuestasResponseDTO;
import com.pruebas.apuestas.dto.ResponseDTO;
import com.pruebas.apuestas.entity.Apuesta;
import com.pruebas.apuestas.entity.Ruleta;
import com.pruebas.apuestas.enums.EColores;
import com.pruebas.apuestas.enums.EEstadoOperacion;
import com.pruebas.apuestas.enums.EEstadosRuleta;
import com.pruebas.apuestas.enums.EMensajes;
import com.pruebas.apuestas.exception.OperacionDenegadaException;
import com.pruebas.apuestas.repository.RuletaRepository;
import com.pruebas.apuestas.service.IRuletaService;

import redis.clients.jedis.Jedis;
@Service
public class RuletaService implements IRuletaService{
	
	private static final Integer NUMERO_MAXIMO_APUESTA = 36;
	private static final Integer NUMERO_MINIMO_APUESTA = 0;
	private static final Integer VALOR_MAXIMO_A_APOSTAR = 10000;
	private static final Integer BENEFICIO_NUMERO_GANADOR = 35;
	private static final Integer BENEFICIO_COLOR_GANADOR = 1;
	
	private Random random = new Random();
	
	@Autowired
	private RuletaRepository ruletaRepository;
	

	@Override
	public Long crearNuevaRuleta() {

		Ruleta ruleta = new Ruleta();
		try(Jedis jedis = new Jedis()){
			Long id = jedis.incr("idRuleta");
			ruleta.setIdRuleta(id);
			ruleta.setEstadoRuleta(EEstadosRuleta.CREADA);
			ruletaRepository.save(ruleta);
		} 
		return ruleta.getIdRuleta();
	}

	@Override
	public ResponseDTO abrirRuleta(Long idRuleta) {
		ResponseDTO responseDTO = new ResponseDTO();
		Optional<Ruleta> optionalRuleta = ruletaRepository.findById(idRuleta);
		
		if(!optionalRuleta.isPresent()) {
			
			responseDTO.setEstadoOperacion(EEstadoOperacion.DENEGADA);
			responseDTO.setMensaje(EMensajes.MSJ_NO_EXISTE_RULETA.getMensaje());
			return responseDTO;
		}
		
		
		Ruleta ruleta = optionalRuleta.get();
		if(ruleta.getEstadoRuleta() != EEstadosRuleta.CREADA) {
			responseDTO.setEstadoOperacion(EEstadoOperacion.DENEGADA);
			responseDTO.setMensaje(EMensajes.MSJ_CAMBIO_DE_ESTADO_NO_PERMITIDO.getMensaje());
			return responseDTO;
		}
		
		ruleta.setEstadoRuleta(EEstadosRuleta.ABIERTA);
		ruletaRepository.save(ruleta);
		
		responseDTO.setEstadoOperacion(EEstadoOperacion.EXITOSA);
		
		return responseDTO;
	}

	@Override
	public ResponseDTO apostar(ApuestaRequestDTO apuestaRequestDTO, Long idUsuario) {
		
		ResponseDTO responseDTO = new ResponseDTO();
		Ruleta ruleta = new Ruleta();
		try {
			validarDatos(apuestaRequestDTO);
			ruleta = obtenerRuleta(apuestaRequestDTO.getIdRuleta());
		} catch (OperacionDenegadaException e) {
			return e.getResponseDTO();
		}
		
		List<Apuesta> listaApuestas = ruleta.getListaApuestas();
		if(listaApuestas == null) {
			listaApuestas = new ArrayList<>();
		}
		Apuesta apuesta = new Apuesta();
		try(Jedis jedis = new Jedis()){
			Long idApuesta = jedis.incr("idApuesta");
			apuesta.setIdApuesta(idApuesta);
			apuesta.setDolaresApostados(apuestaRequestDTO.getDolaresApostados());
			apuesta.setColorApostado(apuestaRequestDTO.getColorApostado());
			apuesta.setNumeroApostado(apuestaRequestDTO.getNumeroApostado());
			apuesta.setIdUsuario(idUsuario);
			listaApuestas.add(apuesta);
			ruleta.setListaApuestas(listaApuestas);
			ruletaRepository.save(ruleta);
		}
		
		responseDTO.setEstadoOperacion(EEstadoOperacion.EXITOSA);
		return responseDTO;
	}

	@Override
	public ApuestasResponseDTO cerrarApuestas(Long idRuleta) {
		ApuestasResponseDTO apuestasResponseDTO = new ApuestasResponseDTO();
		//calcular numero ganador
		Integer numeroGanador = random.nextInt(NUMERO_MAXIMO_APUESTA+1) + NUMERO_MINIMO_APUESTA + 1;
		
		//calcular color ganador
		EColores colorGanador;
		if(numeroGanador.equals(28) || numeroGanador.equals(10)) {
			colorGanador = EColores.NEGRO;
		} else {
			Integer sumaDigitos = calcularSumaDigitos(numeroGanador);
			colorGanador = determinarColorPorSumaDigitos(sumaDigitos);
			
		}
		
		//actualizar ruleta
		Ruleta ruleta = new Ruleta();
		try {
			ruleta = obtenerRuleta(idRuleta);
			validarEstadoRuleta(ruleta);
		} catch (OperacionDenegadaException e) {
			return (ApuestasResponseDTO) e.getResponseDTO();
		}
		
		ruleta.setColorGanador(colorGanador);
		ruleta.setNumeroGanador(numeroGanador);
		ruleta.setEstadoRuleta(EEstadosRuleta.CERRADA);
		
		//establecer las ganancias de las apuestas
		actualizarApuestas(ruleta, numeroGanador, colorGanador);
		
		apuestasResponseDTO.setRuleta(ruleta);
		apuestasResponseDTO.setEstadoOperacion(EEstadoOperacion.EXITOSA);
		
		return apuestasResponseDTO;
	}

	@Override
	public Iterable<Ruleta> consultarRuletas() {
		
		return ruletaRepository.findAll();
	}
	
	/**
	 * 
	 * @param apuestaRequestDTO
	 * @return
	 */
	private boolean diligencioColorYNumero(ApuestaRequestDTO apuestaRequestDTO) {
		return apuestaRequestDTO.getNumeroApostado() != null && apuestaRequestDTO.getNumeroApostado() > 0 &&
				EColores.NINGUNO != apuestaRequestDTO.getColorApostado();
	}
	
	/**
	 * 
	 * @param apuestaRequestDTO
	 * @return
	 */
	private boolean numeroEstaFueraDelRango(ApuestaRequestDTO apuestaRequestDTO) {
		return apuestaRequestDTO.getNumeroApostado() != null && (apuestaRequestDTO.getNumeroApostado() > NUMERO_MAXIMO_APUESTA || apuestaRequestDTO.getNumeroApostado() < NUMERO_MINIMO_APUESTA);
	}
	
	/**
	 * 
	 * @param apuestaRequestDTO
	 * @throws OperacionDenegadaException
	 */
	private void validarDatos(ApuestaRequestDTO apuestaRequestDTO) throws OperacionDenegadaException {
		
		ResponseDTO responseDTO = new ResponseDTO();
		if(diligencioColorYNumero(apuestaRequestDTO)) {
			responseDTO.setEstadoOperacion(EEstadoOperacion.DENEGADA);
			responseDTO.setMensaje(EMensajes.MSJ_APOSTAR_NUMERO_O_COLOR.getMensaje());
			throw new OperacionDenegadaException(responseDTO);
		}
		
		if(numeroEstaFueraDelRango(apuestaRequestDTO) ) {
			responseDTO.setEstadoOperacion(EEstadoOperacion.DENEGADA);
			responseDTO.setMensaje(EMensajes.MSJ_NUMERO_APOSTAR_NO_PERMITIDO.getMensaje());
			throw new OperacionDenegadaException(responseDTO);
		}
		
		if(apuestaRequestDTO.getDolaresApostados() > VALOR_MAXIMO_A_APOSTAR) {
			responseDTO.setEstadoOperacion(EEstadoOperacion.DENEGADA);
			responseDTO.setMensaje(EMensajes.MSJ_SOBREPASA_LIMITE_VALOR.getMensaje());
			throw new OperacionDenegadaException(responseDTO);
		}
		
		if(apuestaRequestDTO.getIdRuleta() == null) {
			responseDTO.setEstadoOperacion(EEstadoOperacion.DENEGADA);
			responseDTO.setMensaje(EMensajes.MSJ_NO_EXISTE_RULETA.getMensaje());
			throw new OperacionDenegadaException(responseDTO);
		}
		
		
	}
	
	/**
	 * 
	 * @param idRuleta
	 * @return
	 * @throws OperacionDenegadaException
	 */
	private Ruleta obtenerRuleta(Long idRuleta) throws OperacionDenegadaException {
		ResponseDTO responseDTO = new ResponseDTO();
		Optional<Ruleta> optionalRuleta = ruletaRepository.findById(idRuleta);
		if(!optionalRuleta.isPresent()) {
			responseDTO.setEstadoOperacion(EEstadoOperacion.DENEGADA);
			responseDTO.setMensaje(EMensajes.MSJ_NO_EXISTE_RULETA.getMensaje());
			throw new OperacionDenegadaException(responseDTO);
		}
		
		return optionalRuleta.get();
	}
	
	private void validarEstadoRuleta(Ruleta ruleta) throws OperacionDenegadaException {
		ResponseDTO responseDTO = new ResponseDTO();
		if(ruleta.getEstadoRuleta() != EEstadosRuleta.ABIERTA) {
			responseDTO.setEstadoOperacion(EEstadoOperacion.DENEGADA);
			responseDTO.setMensaje(EMensajes.MSJ_RULETA_NO_ABIERTA.getMensaje());
			throw new OperacionDenegadaException(responseDTO);
		}
	}
	
	
	/**
	 * permite calcular la suma de los digitos que componen el número
	 * ejemplo: numero = 29  (2 + 9 = 11, 1 + 1 = 2)
	 * @param numero
	 * @return
	 */
	private Integer calcularSumaDigitos(Integer numero) {
		LinkedList<Integer> listaDigitos;
		Integer sumaDigitos = 0;
		do {
			listaDigitos = new LinkedList<>();
			while (numero > 0) {
				listaDigitos.push( numero % 10 );
			    numero = numero / 10;
			}
			sumaDigitos = 0;
			for(Integer digito: listaDigitos) {
				sumaDigitos += digito;
			}
			numero = sumaDigitos;
		}while (sumaDigitos.toString().length() > 1);
		
		return sumaDigitos;
	}
	
	/**
	 * permite determinar el color ganador de acuerdo a si la suma de los digitos es par o impar
	 * (impar = ROJO, par = NEGRO)
	 * @param sumaDigitos
	 * @return
	 */
	private EColores determinarColorPorSumaDigitos(Integer sumaDigitos) {
		EColores colorGanador;
		
		if(sumaDigitos%2 == 0) {
			colorGanador = EColores.NEGRO;
		} else {
			colorGanador = EColores.ROJO;
		}
		
		return colorGanador;
	}
	
	/**
	 * permite actualizar el resultado de las apuestas de acuerdo al numero y color ganador
	 * @param idRuleta
	 * @param numeroGanador
	 * @param colorGanador
	 */
	private void actualizarApuestas(Ruleta ruleta, Integer numeroGanador, EColores colorGanador) {
		//actualizar apuestas
		List<Apuesta> listApuestasActualizado = new ArrayList<>();
		Iterable<Apuesta> listApuestas = ruleta.getListaApuestas();
		
		Integer resultadoApuesta = 0;
		for(Apuesta apuesta: listApuestas) {
			if(apuesta.getNumeroApostado() != null) {
				resultadoApuesta = apuesta.getNumeroApostado().equals(numeroGanador) ? apuesta.getDolaresApostados()*BENEFICIO_NUMERO_GANADOR : 0;
				apuesta.setResultadoApuesta(resultadoApuesta);
			} else if (EColores.NINGUNO != apuesta.getColorApostado()) {
				resultadoApuesta = apuesta.getColorApostado().equals(colorGanador) ? apuesta.getDolaresApostados()*BENEFICIO_COLOR_GANADOR : 0;
				apuesta.setResultadoApuesta(resultadoApuesta);
			}
			listApuestasActualizado.add(apuesta);
		}
		ruleta.setListaApuestas(listApuestasActualizado);
		ruletaRepository.save(ruleta);
		
		
	}

	

}
