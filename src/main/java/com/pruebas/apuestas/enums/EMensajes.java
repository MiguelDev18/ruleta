package com.pruebas.apuestas.enums;

public enum EMensajes {
	
	MSJ_NO_EXISTE_RULETA("No se encuentra la ruleta"),
	MSJ_CAMBIO_DE_ESTADO_NO_PERMITIDO("No se puede abrir la ruleta debido a su estado"),
	MSJ_APOSTAR_NUMERO_O_COLOR("solo se puede apostar o a número o a color"),
	MSJ_NUMERO_APOSTAR_NO_PERMITIDO("no se puede apostar a ese número"),
	MSJ_SOBREPASA_LIMITE_VALOR("sobrepasa el valor máximo a apostar"),
	MSJ_RULETA_NO_ABIERTA("la ruleta no se puede cerrar porque no esta abierta");
	
	private final String mensaje;

	private EMensajes(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getMensaje() {
		return mensaje;
	}
	
	

}
