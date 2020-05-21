package com.pruebas.apuestas.entity;

import java.io.Serializable;

import com.pruebas.apuestas.enums.EColores;

public class Apuesta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long idApuesta;	
	private Long idUsuario;
	private EColores colorApostado;
	private Integer numeroApostado;
	private Integer dolaresApostados;
	private Integer resultadoApuesta;
	public Long getIdApuesta() {
		return idApuesta;
	}
	public void setIdApuesta(Long idApuesta) {
		this.idApuesta = idApuesta;
	}
	public Long getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}
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
	public Integer getResultadoApuesta() {
		return resultadoApuesta;
	}
	public void setResultadoApuesta(Integer resultadoApuesta) {
		this.resultadoApuesta = resultadoApuesta;
	}
	@Override
	public String toString() {
		return "Apuesta [idApuesta=" + idApuesta + ", idUsuario=" + idUsuario + ", colorApostado=" + colorApostado
				+ ", numeroApostado=" + numeroApostado + ", dolaresApostados=" + dolaresApostados
				+ ", resultadoApuesta=" + resultadoApuesta + "]";
	}
	
	
	
	
	
	
	
	

}
