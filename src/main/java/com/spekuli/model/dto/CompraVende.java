package com.spekuli.model.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CompraVende implements Serializable {
	private static final long serialVersionUID = 1L;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	private String data;
	private boolean maximo;
	private double valor;
	private int pregao;
	
	public CompraVende() {}

	public CompraVende(Date data, boolean maximo, double valor, int pregao) {
		setData(data);
		this.maximo = maximo;
		this.valor = valor;
		this.pregao = pregao;
	}
		
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	public void setData(Date data) {
		this.data = sdf.format(data);
	}

	public boolean isMaximo() {
		return maximo;
	}

	public void setMaximo(boolean maximo) {
		this.maximo = maximo;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public int getPregao() {
		return pregao;
	}

	public void setPregao(int pregao) {
		this.pregao = pregao;
	}

	
}
