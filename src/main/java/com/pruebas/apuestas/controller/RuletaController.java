package com.pruebas.apuestas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.pruebas.apuestas.dto.ApuestaRequestDTO;
import com.pruebas.apuestas.dto.ApuestasResponseDTO;
import com.pruebas.apuestas.dto.ResponseDTO;
import com.pruebas.apuestas.entity.Ruleta;
import com.pruebas.apuestas.service.IRuletaService;

@RestController
public class RuletaController {
	
	
	@Autowired
	IRuletaService ruletaService;
	
	
	@GetMapping(value = "/ruleta/consultar")
	public Iterable<Ruleta> consultarRuletas() {
		
		return ruletaService.consultarRuletas();
	}
	
	@GetMapping(value = "/ruleta/crear")
	public Long crearNuevaRuleta() {
		
		return ruletaService.crearNuevaRuleta();
	}
	
	@PostMapping(value = "/ruleta/abrir")
	public ResponseDTO crearNuevaRuleta(@RequestBody Long idRuleta) {
		
		return ruletaService.abrirRuleta(idRuleta);
	}
	
	@PostMapping(value = "/ruleta/apostar")
	public ResponseDTO apostar(@RequestHeader Long idUsuario, @RequestBody ApuestaRequestDTO apuestaRequestDTO) {
		
		return ruletaService.apostar(apuestaRequestDTO,idUsuario);
	}
	
	@PostMapping(value = "/ruleta/cerrar")
	public ApuestasResponseDTO cerrar(@RequestBody Long idRuleta) {
		
		
		return ruletaService.cerrarApuestas(idRuleta);
	}
	


}
