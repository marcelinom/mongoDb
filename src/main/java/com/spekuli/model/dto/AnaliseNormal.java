package com.spekuli.model.dto;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AnaliseNormal implements Serializable {
	private static final long serialVersionUID = 1L;
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private static DecimalFormat df = new DecimalFormat("#.##");

	static {
		df.setRoundingMode(RoundingMode.CEILING);
	}
	
	private Date data;
	private Double comprar;
	private Double vender;
	private Double bollinger;
	private Double rsi;
	private Double macd;
	private Double stk;
	private Double obv;
	private Double futuro;
	private Double bolsa;
	
	public AnaliseNormal() {}
	
	public AnaliseNormal(Date data, Double bollinger, Double rsi, Double macd, Double stk, Double obv, Double futuro, Double bolsa) {
		this.data = data;
		this.bollinger = bollinger;
		this.rsi = rsi;
		this.macd = macd;
		this.stk = stk;
		this.obv = obv;
		this.futuro = futuro;
		this.bolsa = bolsa;
	}
	public Double getBollinger() {
		return bollinger;
	}
	public void setBollinger(Double bollinger) {
		this.bollinger = bollinger;
	}
	public Double getRsi() {
		return rsi;
	}
	public void setRsi(Double rsi) {
		this.rsi = rsi;
	}
	public Double getObv() {
		return obv;
	}

	public void setObv(Double obv) {
		this.obv = obv;
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

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Double getComprar() {
		return comprar;
	}

	public void setComprar(Double comprar) {
		this.comprar = comprar;
	}

	public Double getVender() {
		return vender;
	}

	public void setVender(Double vender) {
		this.vender = vender;
	}

	public Double getFuturo() {
		return futuro;
	}

	public void setFuturo(Double futuro) {
		this.futuro = futuro;
	}

	public Double getBolsa() {
		return bolsa;
	}

	public void setBolsa(Double bolsa) {
		this.bolsa = bolsa;
	}

	public boolean temIndicadorNulo() {
		return bollinger==null || rsi==null || macd==null || stk==null || obv==null || futuro==null || bolsa==null;
	}
	public void converterParaZeroUm() {
		rsi = (rsi+1)/2;
		macd = (macd+1)/2;
		stk = (stk+1)/2;
		bollinger = (bollinger+1)/2;
		futuro = (futuro+1)/2;
		bolsa = (bolsa+1)/2;
		obv = (obv+1)/2;
	}
	public double[] parametros() {
		return new double[]{bollinger, macd, rsi, stk, obv, futuro, bolsa}; 
	}
	public double[] resultado() {
		return new double[]{comprar, vender}; 
	}
	@Override
	public String toString() {
		return "Data:"+sdf.format(data)+
				" Bollinger:"+(bollinger==null?null:df.format(bollinger))+
				" RSI:"+(rsi==null?null:df.format(rsi))+
				" MACD:"+(macd==null?null:df.format(macd))+
				" Stocastic:"+(stk==null?null:df.format(stk))+
				" OBV:"+(obv==null?null:df.format(obv))+
				" Fututo:"+(futuro==null?null:df.format(futuro))+
				" Comprar:"+(comprar==null?null:df.format(comprar))+
				" Vender:"+(vender==null?null:df.format(vender));
	}

}
