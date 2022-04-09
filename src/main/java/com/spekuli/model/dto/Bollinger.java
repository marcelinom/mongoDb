package com.spekuli.model.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Bollinger implements Serializable {
	private static final long serialVersionUID = 1L;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	private String data;
	private double lower;
	private double middle;
	private double upper;
	
	public Bollinger(Date data, double lower, double middle, double upper) {
		setData(data);
		this.lower = lower;
		this.middle = middle;
		this.upper = upper;
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
	public double getLower() {
		return lower;
	}
	public void setLower(double lower) {
		this.lower = lower;
	}
	public double getMiddle() {
		return middle;
	}
	public void setMiddle(double middle) {
		this.middle = middle;
	}
	public double getUpper() {
		return upper;
	}
	public void setUpper(double upper) {
		this.upper = upper;
	}
	
}
