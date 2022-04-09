package com.spekuli.model.dto;

import java.io.Serializable;
import java.util.List;

public class GanhoCarteira implements Serializable {
	private static final long serialVersionUID = 1L;

	private double aporte;
	private double retorno;
	private double ganhoMedio;			// ganho medio, em %, desconsiderando taxas e impostos
	private double custos;
	private int transacoes;
	private List<Ganho> acoes;
	
	public GanhoCarteira() {}

	public GanhoCarteira(double aporte, double retorno, double ganho, double custos, int transacoes, List<Ganho> acoes) {
		this.aporte = aporte;
		this.retorno = retorno;
		this.ganhoMedio = ganho;
		this.custos = custos;
		this.transacoes = transacoes;
		this.acoes = acoes;
	}

	public double getAporte() {
		return aporte;
	}

	public void setAporte(double aporte) {
		this.aporte = aporte;
	}

	public double getRetorno() {
		return retorno;
	}

	public void setRetorno(double retorno) {
		this.retorno = retorno;
	}

	public int getTransacoes() {
		return transacoes;
	}

	public void setTransacoes(int transacoes) {
		this.transacoes = transacoes;
	}

	public double getGanhoMedio() {
		return ganhoMedio;
	}

	public void setGanhoMedio(double ganhoMedio) {
		this.ganhoMedio = ganhoMedio;
	}

	public double getCustos() {
		return custos;
	}

	public void setCustos(double custos) {
		this.custos = custos;
	}

	public List<Ganho> getAcoes() {
		return acoes;
	}

	public void setAcoes(List<Ganho> acoes) {
		this.acoes = acoes;
	}

	
}
