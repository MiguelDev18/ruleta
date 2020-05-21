package com.pruebas.apuestas.exception;

import com.pruebas.apuestas.dto.ResponseDTO;

public class OperacionDenegadaException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final ResponseDTO responseDTO;

	public OperacionDenegadaException(ResponseDTO responseDTO) {
		super();
		this.responseDTO = responseDTO;
	}

	public ResponseDTO getResponseDTO() {
		return responseDTO;
	}
	
	

}
