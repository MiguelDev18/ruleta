package com.pruebas.apuestas.dto;

import com.pruebas.apuestas.enums.EColores;

public class ApuestaRequestDTO {
	
	private EColores colorApostado;
	private Integer numeroApostado;
	private Integer dolaresApostados;
	private Long idRuleta;
	public EColores getColorApostado() {
		return colorApostado;
	}
	public void setColorApostado(EColores colorApostado) {
		this.colorApostado = colorApostado;
	}
	public Integer getNumeroApostado() {
		return numeroApostado;
	}
	public void setNumeroApostado(Integer numeroApostado) {
		this.numeroApostado = numeroApostado;
	}
	public Integer getDolaresApostados() {
		return dolaresApostados;
	}
	public void setDolaresApostados(Integer dolaresApostados) {
		this.dolaresApostados = dolaresApostados;
	}
	public Long getIdRuleta() {
		return idRuleta;
	}
	public void setIdRuleta(Long idRuleta) {
		this.idRuleta = idRuleta;
	}
	@Override
	public String toString() {
		return "ApuestaRequestDTO [colorApostado=" + colorApostado + ", numeroApostado=" + numeroApostado
				+ ", dolaresApostados=" + dolaresApostados + ", idRuleta=" + idRuleta + "]";
	}
	
	

}
