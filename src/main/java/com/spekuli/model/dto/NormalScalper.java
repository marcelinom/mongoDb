package com.spekuli.model.dto;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NormalScalper implements Serializable {
	private static final long serialVersionUID = 1L;
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private static DecimalFormat df = new DecimalFormat("#.##");

	static {
		df.setRoundingMode(RoundingMode.CEILING);
	}
	
	private Date data;
	private Double comprar;
	private Double dma;				// media simples do delta relativo
	private Double macd;
	private Double bollinger;
	private Double stk;
	private Double delta;	
	
	public NormalScalper() {}
	
	public NormalScalper(Date data, Double dma, Double bollinger, Double macd, Double stk, Double delta) {
		this.data = data;
		this.dma = dma;
		this.bollinger = bollinger;
		this.macd = macd;
		this.stk = stk;
		this.delta = delta;
	}
	
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Double getBollinger() {
		return bollinger;
	}

	public void setBollinger(Double bollinger) {
		this.bollinger = bollinger;
	}

	public Double getDma() {
		return dma;
	}

	public void setDma(Double dma) {
		this.dma = dma;
	}

	public Double getMacd() {
		return macd;
	}

	public void setMacd(Double macd) {
		this.macd = macd;
	}

	public Double getStk() {
		return stk;
	}

	public void setStk(Double stk) {
		this.stk = stk;
	}

	public Double getObv() {
		return delta;
	}

	public void setObv(Double obv) {
		this.delta = obv;
	}

	public Double getComprar() {
		return comprar;
	}

	public void setComprar(Double comprar) {
		this.comprar = comprar;
	}

	public boolean temIndicadorNulo() {
		return dma==null || macd==null || stk==null || delta==null;

	}
	public double[] parametros() {
		return new double[]{macd, dma, stk, delta}; 
	}
	public double[] resultado() {
		return new double[]{comprar}; 
	}
	public void converterParaZeroUm() {
		dma = (dma+1)/2;
		macd = (macd+1)/2;
		stk = (stk+1)/2;
		delta = (delta+1)/2;
	}
	@Override
	public String toString() {
		return "Data:"+sdf.format(data)+
				" RSI:"+(dma==null?null:df.format(dma))+
				" MACD:"+(macd==null?null:df.format(macd))+
				" Stocastic:"+(stk==null?null:df.format(stk))+
				" OBV:"+(delta==null?null:df.format(delta))+
				" Comprar:"+(comprar==null?null:df.format(comprar));
	}

}
