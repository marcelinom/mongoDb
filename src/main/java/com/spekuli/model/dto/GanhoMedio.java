package com.spekuli.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class GanhoMedio implements Serializable, Comparable<GanhoMedio> {
	private static final long serialVersionUID = 1L;

	private int timeFrame;
	private double ganho;
	private double prazo;
	
	public GanhoMedio() {}
	
	public GanhoMedio(int timeFrame, double ganho, double prazo) {
		this.timeFrame = timeFrame;
		this.prazo = prazo;
		this.ganho = ganho;
	}
	
	public GanhoMedio(int timeFrame, BigDecimal ganho, BigDecimal prazo) {
		this.timeFrame = timeFrame;
		this.ganho = ganho.doubleValue();
		this.prazo = prazo.doubleValue();
	}
	
	public int getTimeFrame() {
		return timeFrame;
	}
	public void setTimeFrame(int timeFrame) {
		this.timeFrame = timeFrame;
	}

	public double getGanho() {
		return ganho;
	}

	public void setGanho(double ganho) {
		this.ganho = ganho;
	}

	public double getPrazo() {
		return prazo;
	}

	public void setPrazo(double prazo) {
		this.prazo = prazo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(ganho);
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
		GanhoMedio other = (GanhoMedio) obj;
		if (Double.doubleToLongBits(ganho) != Double.doubleToLongBits(other.ganho))
			return false;
		return true;
	}

	@Override
	public int compareTo(GanhoMedio outro) {
		return new Double(outro.ganho/outro.prazo).compareTo(ganho/prazo);
	}
	
}
