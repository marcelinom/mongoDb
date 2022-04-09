package com.spekuli.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class GanhoDicas implements Serializable {
	private static final long serialVersionUID = 1L;

	private int timeFrame;
	private double soma;
	private double media;
	private double min;
	private double max;
	
	public GanhoDicas() {}
	
	public GanhoDicas(int timeFrame, double soma, double media, double min, double max) {
		this.timeFrame = timeFrame;
		this.soma = soma;
		this.media = media;
		this.min = min;
		this.max = max;
	}
	
	public GanhoDicas(int timeFrame, BigDecimal soma, double media, BigDecimal min, BigDecimal max) {
		this.timeFrame = timeFrame;
		this.soma = soma.doubleValue();
		this.media = media;
		this.min = min.doubleValue();
		this.max = max.doubleValue();
	}
	
	public int getTimeFrame() {
		return timeFrame;
	}
	public void setTimeFrame(int timeFrame) {
		this.timeFrame = timeFrame;
	}
	public double getSoma() {
		return soma;
	}
	public void setSoma(double soma) {
		this.soma = soma;
	}
	public double getMedia() {
		return media;
	}
	public void setMedia(double media) {
		this.media = media;
	}
	public double getMin() {
		return min;
	}
	public void setMin(double min) {
		this.min = min;
	}
	public double getMax() {
		return max;
	}
	public void setMax(double max) {
		this.max = max;
	}	
	
}
