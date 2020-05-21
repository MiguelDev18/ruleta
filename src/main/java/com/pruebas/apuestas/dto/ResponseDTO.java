package com.pruebas.apuestas.dto;

import java.io.Serializable;

import com.pruebas.apuestas.enums.EEstadoOperacion;

public class ResponseDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EEstadoOperacion estadoOperacion;
	private String mensaje;
	public EEstadoOperacion getEstadoOperacion() {
		return estadoOperacion;
	}
	public void setEstadoOperacion(EEstadoOperacion estadoOperacion) {
		this.estadoOperacion = estadoOperacion;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	@Override
	public String toString() {
		return "ResponseDTO [estadoOperacion=" + estadoOperacion + ", mensaje=" + mensaje + "]";
	}
	

}
