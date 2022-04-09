package com.spekuli.model.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class Oferta implements Serializable {
	private static final long serialVersionUID = 1L;
    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyddMMHHmm");
    
	private int qtd;
	private double valor;  
	
	public Oferta(String qtd, String valor) {
		this.qtd = Integer.parseInt(qtd);
		this.valor = Double.parseDouble(valor);
	}

	public int getQtd() {
		return qtd;
	}

	public void setQtd(int qtd) {
		this.qtd = qtd;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}
	
	public void atualiza(String qtd, String valor) {
		this.qtd = Integer.parseInt(qtd);
		this.valor = Double.parseDouble(valor);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(valor);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Oferta other = (Oferta) obj;
		if (Double.doubleToLongBits(valor) != Double.doubleToLongBits(other.valor))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[qtd=" + qtd + ", valor=" + valor + "]";
	}
	
}
