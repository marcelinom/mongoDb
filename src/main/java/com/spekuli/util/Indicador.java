package com.spekuli.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.indicators.CachedIndicator;

public class Indicador implements Serializable {
	private static final long serialVersionUID = 1L;

	private String tipo;
	private String periodo;
	private List<Decimal> valores;
	
	public Indicador() {}
	
	public Indicador(String periodo, CachedIndicator<Decimal> indicador) {
		this.tipo = indicador.getClass().getSimpleName();
		this.periodo = periodo==null?"":periodo;
		this.valores = calcula(indicador);
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public List<Decimal> calcula(CachedIndicator<Decimal> indicador) {
		List<Decimal> lista = new ArrayList<Decimal>();
		if (indicador != null && indicador.getTimeSeries() != null) {
			for (int i=0; i<indicador.getTimeSeries().getTickCount(); i++) {
				lista.add(indicador.getValue(i));
			}
		}
		return lista;
	}

	public List<Decimal> getValores() {
		return valores;
	}

	public void setValores(List<Decimal> valores) {
		this.valores = valores;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
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
		Indicador other = (Indicador) obj;
		if (tipo == null) {
			if (other.tipo != null)
				return false;
		} else if (!tipo.equals(other.tipo))
			return false;
		return true;
	}

}
