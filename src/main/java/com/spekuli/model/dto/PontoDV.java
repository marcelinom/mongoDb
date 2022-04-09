package com.spekuli.model.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PontoDV implements Serializable {
	private static final long serialVersionUID = 1L;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	private String data;
	private double valor;
	
	public PontoDV(String data, double valor) {
		this.data = data;
		this.valor = valor;
	}
	public PontoDV(String data, long valor) {
		this.data = data;
		this.valor = valor;
	}
	public PontoDV(Date data, double valor) {
		setData(data);
		this.valor = valor;
	}
	public String getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = sdf.format(data);
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
		
}
