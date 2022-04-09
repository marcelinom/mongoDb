package com.spekuli.model.dto;

import java.io.Serializable;

public class Painel implements Serializable, Comparable<Painel> {
	private static final long serialVersionUID = 1L;
	public static double BUY_MIN = 0.4;
	public static double MM_PB_MIN = 0.5;

	private String codigo;
	private String hora;
	private double valor;
	private double var;
	private long negocios;
	private double deltaMedio;
	private double compRel;
	private double bBW;
	private double ppb;
	private double npb;
	private double pb;
	private double emaPB;
	private double sinal;
	private int qtde;
	private boolean mudou;
	
	public Painel(String codigo, String hora, double valor, double var, long negocios, double compRel) {
		this.codigo = codigo;
		this.hora = hora;
		this.valor = valor;
		this.var = var;
		this.negocios = negocios;
		this.compRel = compRel;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public boolean isMudou() {
		return mudou;
	}
	public void setMudou(boolean mudou) {
		this.mudou = mudou;
	}
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}
	public double getSinal() {
		return sinal;
	}
	public void setSinal(double sinal) {
		this.sinal = sinal;
	}
	public double getPb() {
		return pb;
	}
	public void setPb(double pb) {
		this.pb = pb;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	public int getQtde() {
		return qtde;
	}
	public void setQtde(int qtde) {
		this.qtde = qtde;
	}
	public double getEmaPB() {
		return emaPB;
	}
	public void setEmaPB(double emaPB) {
		this.emaPB = emaPB;
	}
	public double getNpb() {
		return npb;
	}
	public void setNpb(double npb) {
		this.npb = npb;
	}
	public double getPpb() {
		return ppb;
	}
	public void setPpb(double ppb) {
		this.ppb = ppb;
	}
	public double getbBW() {
		return bBW;
	}
	public void setbBW(double bBW) {
		this.bBW = bBW;
	}
	public double getCompRel() {
		return compRel;
	}
	public void setCompRel(double compRel) {
		this.compRel = compRel;
	}
	public double getDeltaMedio() {
		return deltaMedio;
	}
	public void setDeltaMedio(double deltaMedio) {
		this.deltaMedio = deltaMedio;
	}
	public double getVar() {
		return var;
	}
	public void setVar(double var) {
		this.var = var;
	}
	public long getNegocios() {
		return negocios;
	}
	public void setNegocios(long negocios) {
		this.negocios = negocios;
	}
	public double calculaCriterio() {
		double t = getPpb()-getNpb();
		return t>0?Math.log(1+t):(t<0?-Math.log(1-t):0) + getEmaPB() + getPb() * getbBW() / 2;
	}
	public boolean selecionar() {
		return getEmaPB()>MM_PB_MIN	&& (getPpb()-getNpb())>=0 && getSinal()>=0 && getDeltaMedio()>=0 && getCompRel()>BUY_MIN;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		if (compareTo((Painel)obj)==0) return true;
		return false;
	}
	@Override
	public int compareTo(Painel o) {
		double c = calculaCriterio()-o.calculaCriterio();
		if (c>0) return -1;
		else if (c<0) return 1;
		else return 0;
	}
	
}
