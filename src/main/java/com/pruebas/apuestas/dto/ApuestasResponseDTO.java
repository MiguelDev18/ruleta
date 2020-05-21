package com.pruebas.apuestas.dto;

import com.pruebas.apuestas.entity.Ruleta;

public class ApuestasResponseDTO extends ResponseDTO {
	
	private Ruleta ruleta;

	public Ruleta getRuleta() {
		return ruleta;
	}

	public void setRuleta(Ruleta ruleta) {
		this.ruleta = ruleta;
	}

	@Override
	public String toString() {
		return "ApuestasResponseDTO [ruleta=" + ruleta + "]";
	}
	
	

}
