package com.spekuli.model.dto;

import java.io.Serializable;

public class Ganho implements Serializable {
	private static final long serialVersionUID = 1L;

	private String acao;
	private double media;					// em %
	private double estimado;				// comparacao com o lucro estimado, em %
	private int lucrativas;
	private int deficitarias;
	
	public Ganho(String acao) {
		this.acao = acao;
	}
	public Ganho(String acao, double media, double estimado, int lucrativas, int deficitarias) {
		this.acao = acao;
		this.media = media;
		this.estimado = estimado;
		this.lucrativas = lucrativas;
		this.deficitarias = deficitarias;
	}
	public String getAcao() {
		return acao;
	}
	public void setAcao(String acao) {
		this.acao = acao;
	}
	public double getMedia() {
		return media;
	}
	public void setMedia(double media) {
		this.media = media;
	}
	public double getEstimado() {
		return estimado;
	}
	public void setEstimado(double estimado) {
		this.estimado = estimado;
	}
	public int getLucrativas() {
		return lucrativas;
	}
	public void setLucrativas(int lucrativas) {
		this.lucrativas = lucrativas;
	}
	public int getDeficitarias() {
		return deficitarias;
	}
	public void setDeficitarias(int deficitarias) {
		this.deficitarias = deficitarias;
	}
			
}
