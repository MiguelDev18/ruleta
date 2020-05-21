package com.pruebas.apuestas.entity;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import com.pruebas.apuestas.enums.EColores;
import com.pruebas.apuestas.enums.EEstadosRuleta;
@RedisHash("Ruleta")
public class Ruleta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private Long idRuleta;
	private EEstadosRuleta estadoRuleta;
	private Integer numeroGanador;
	private EColores colorGanador;
	private List<Apuesta> listaApuestas;
	public Long getIdRuleta() {
		return idRuleta;
	}
	public void setIdRuleta(Long idRuleta) {
		this.idRuleta = idRuleta;
	}
	public EEstadosRuleta getEstadoRuleta() {
		return estadoRuleta;
	}
	public void setEstadoRuleta(EEstadosRuleta estadoRuleta) {
		this.estadoRuleta = estadoRuleta;
	}
	public Integer getNumeroGanador() {
		return numeroGanador;
	}
	public void setNumeroGanador(Integer numeroGanador) {
		this.numeroGanador = numeroGanador;
	}
	public EColores getColorGanador() {
		return colorGanador;
	}
	public void setColorGanador(EColores colorGanador) {
		this.colorGanador = colorGanador;
	}
	public List<Apuesta> getListaApuestas() {
		return listaApuestas;
	}
	public void setListaApuestas(List<Apuesta> listaApuestas) {
		this.listaApuestas = listaApuestas;
	}
	@Override
	public String toString() {
		return "Ruleta [idRuleta=" + idRuleta + ", estadoRuleta=" + estadoRuleta + ", numeroGanador=" + numeroGanador
				+ ", colorGanador=" + colorGanador + ", listaApuestas=" + listaApuestas + "]";
	}
	
	
	
	

}
