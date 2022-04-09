package com.spekuli.model.dto;

import java.io.Serializable;
import java.util.List;

public class AnaliseGrafica implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String nome;
	private List<Cotacao> cotacoes;
	private List<Linha> supstances;
	private List<Linha> trends;
	private List<Linha> trendsG;
	private List<Bollinger> bollinger;
	private List<PontoDV> sma20;
	private List<PontoDV> sma50;
	private List<PontoDV> sma200;
	private List<PontoDV> gaps;
	private List<Tripla> stk;
	private List<PontoDV> obv;
	private List<PontoDV> rsi;
	private List<Tripla> macd;
	private List<CompraVende> quando;
	
	public AnaliseGrafica() {}

	public AnaliseGrafica(String nome) {
		this.nome = nome;
	}

	public AnaliseGrafica(List<Cotacao> cotacoes, List<Linha> supstances, List<Linha> trends) {
		this.cotacoes = cotacoes;
		this.supstances = supstances;
		this.trends = trends;
	}
	public List<Cotacao> getCotacoes() {
		return cotacoes;
	}
	public void setCotacoes(List<Cotacao> cotacoes) {
		this.cotacoes = cotacoes;
	}
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Linha> getSupstances() {
		return supstances;
	}
	public void setSupstances(List<Linha> supstances) {
		this.supstances = supstances;
	}
	public List<PontoDV> getGaps() {
		return gaps;
	}
	public void setGaps(List<PontoDV> gaps) {
		this.gaps = gaps;
	}
	public List<Linha> getTrends() {
		return trends;
	}
	public void setTrends(List<Linha> trends) {
		this.trends = trends;
	}

	public List<Linha> getTrendsG() {
		return trendsG;
	}

	public void setTrendsG(List<Linha> trendsG) {
		this.trendsG = trendsG;
	}

	public List<PontoDV> getRsi() {
		return rsi;
	}

	public void setRsi(List<PontoDV> rsi) {
		this.rsi = rsi;
	}

	public List<Tripla> getStk() {
		return stk;
	}

	public void setStk(List<Tripla> stk) {
		this.stk = stk;
	}

	public List<Bollinger> getBollinger() {
		return bollinger;
	}
	public void setBollinger(List<Bollinger> bollinger) {
		this.bollinger = bollinger;
	}
	public List<PontoDV> getSma20() {
		return sma20;
	}
	public void setSma20(List<PontoDV> sma21) {
		this.sma20 = sma21;
	}
	public List<PontoDV> getSma50() {
		return sma50;
	}
	public void setSma50(List<PontoDV> sma50) {
		this.sma50 = sma50;
	}
	public List<PontoDV> getSma200() {
		return sma200;
	}
	public void setSma200(List<PontoDV> sma200) {
		this.sma200 = sma200;
	}

	public List<Tripla> getMacd() {
		return macd;
	}

	public void setMacd(List<Tripla> macd) {
		this.macd = macd;
	}

	public List<CompraVende> getQuando() {
		return quando;
	}

	public void setQuando(List<CompraVende> quando) {
		this.quando = quando;
	}

	public List<PontoDV> getObv() {
		return obv;
	}

	public void setObv(List<PontoDV> obv) {
		this.obv = obv;
	}

}
