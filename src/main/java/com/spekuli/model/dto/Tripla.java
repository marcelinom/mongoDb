package com.spekuli.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tripla implements Serializable {
	private static final long serialVersionUID = 1L;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	private String data;
	private double valorA;
	private double valorB;
	
	public Tripla() {}
	
	public Tripla(String data, BigDecimal valorA, long valorB) {
		this.data = data;
		this.valorA = valorA.doubleValue();
		this.valorB = valorB;
	}
	public Tripla(Date data, double valorA, double valorB) {
		setData(data);
		this.valorA = valorA;
		this.valorB = valorB;
	}
	public String getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = sdf.format(data);
	}
	public void setData(String data) {
		this.data = data;
	}
	public double getValorA() {
		return valorA;
	}
	public void setValorA(double valorA) {
		this.valorA = valorA;
	}
	public double getValorB() {
		return valorB;
	}
	public void setValorB(double valorB) {
		this.valorB = valorB;
	}
	
}
