package com.spekuli.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Dica implements Serializable {
	private static final long serialVersionUID = 1L;
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	public enum Operacao{COMPRAR, MANTER, VENDER};

	private String acao;
	private String data;
	private Operacao oper;
	private double precisao;			// em %
	private double prazoMedio;			// em pregoes
	private double ganhoMedio;			// em %
	private double valor;				// valor medio do pregao na data
	private double stop;				// valor minimo p manter o papel
	private double alvo;				// valor maximo p manter o papel
	private int timeFrame;			
	
	public Dica() {}

	public Dica(String acao) {
		this.acao = acao;
	}
	
	public Dica(String acao, double valor, Operacao oper) {
		this.acao = acao;
		this.valor = valor;
		this.oper = oper;
	}
	
	public Dica(String acao, Date data, double precisao, double prazoMedio, double ganhoMedio, double valor, double stop, int timeFrame) {
		this.acao = acao;
		this.data = sdf.format(data);
		this.precisao = precisao;
		this.prazoMedio = prazoMedio;
		this.ganhoMedio = ganhoMedio;
		this.valor = valor;
		this.stop = round(valor*(1-Math.min(0.05, Math.max(0.01, stop)))-0.01, 2);
		this.alvo = round(valor*(1+ganhoMedio/100), 2);
		this.timeFrame = timeFrame;
	}
	
	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public Operacao getOper() {
		return oper;
	}

	public void setOper(Operacao oper) {
		this.oper = oper;
	}

	public double getStop() {
		return stop;
	}

	public void setStop(double stop) {
		this.stop = stop;
	}

	public double getAlvo() {
		return alvo;
	}

	public void setAlvo(double alvo) {
		this.alvo = alvo;
	}

	public String getAcao() {
		return acao;
	}
	public void setAcao(String acao) {
		this.acao = acao;
	}
	public String getData() {
		return data;
	}
	public Date data() {
		try {
			return sdf.parse(data);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public void setData(String data) {
		this.data = data;
	}
	public void setData(Date data) {
		this.data = sdf.format(data);
	}
	public double getPrecisao() {
		return precisao;
	}
	public void setPrecisao(double precisao) {
		this.precisao = precisao;
	}
	public double getPrazoMedio() {
		return prazoMedio;
	}
	public void setPrazoMedio(double prazoMedio) {
		this.prazoMedio = prazoMedio;
	}
	public double getGanhoMedio() {
		return ganhoMedio;
	}
	public void setGanhoMedio(double ganhoMedio) {
		this.ganhoMedio = ganhoMedio;
	}
	
	public int getTimeFrame() {
		return timeFrame;
	}

	public void setTimeFrame(int timeFrame) {
		this.timeFrame = timeFrame;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acao == null) ? 0 : acao.hashCode());
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
		Dica other = (Dica) obj;
		if (acao == null) {
			if (other.acao != null)
				return false;
		} else if (!acao.equals(other.acao))
			return false;
		return true;
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}	

}
