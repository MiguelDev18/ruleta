package com.pruebas.apuestas.service;

import com.pruebas.apuestas.dto.ApuestaRequestDTO;
import com.pruebas.apuestas.dto.ApuestasResponseDTO;
import com.pruebas.apuestas.dto.ResponseDTO;
import com.pruebas.apuestas.entity.Ruleta;

public interface IRuletaService {
	/**
	 * crea la ruleta dando como resultado el idRuleta
	 * @return
	 */
	Long crearNuevaRuleta();
	/**
	 * cambia el estado de la ruleta, de creada a abierta
	 * @param idRuleta
	 * @return
	 */
	ResponseDTO abrirRuleta(Long idRuleta);
	/**
	 * permite apostar una cantidad de dinero a un numero o color de una ruleta
	 * @param apuestaRequestDTO
	 * @return
	 */
	ResponseDTO apostar(ApuestaRequestDTO apuestaRequestDTO, Long idUsuario);
	
	/**
	 * permite realizar el cierre de apuestas, generando el resultado de las apuestas.
	 * @param idRuleta
	 * @return
	 */
	ApuestasResponseDTO cerrarApuestas(Long idRuleta);
	/**
	 * permite consultar todas las ruletas creadas.
	 * @return
	 */
	Iterable<Ruleta> consultarRuletas();

	
	

}
