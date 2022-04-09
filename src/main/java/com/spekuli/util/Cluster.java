package com.spekuli.util;

import java.util.Date;
import java.util.TreeSet;

import com.google.common.collect.Range;

import eu.verdelhan.ta4j.Decimal;

public class Cluster {
	private int pregao;
	private Boolean maxima;
	private Decimal base;
	private Range<Decimal> range;
	private TreeSet<Integer> ids;
	private Date primOcorr;
	private Date ultimaOcorr;
	
	public Cluster(int pregao) {
		this.pregao = pregao;
		ids = new TreeSet<Integer>();
	}
	
	public Cluster(int pregao, Boolean maxima, Decimal base, Range<Decimal> range, Date primOcorr) {
		this.pregao = pregao;
		this.maxima = maxima;
		this.base = base;
		this.range = range;
		this.primOcorr = primOcorr;
		ids = new TreeSet<Integer>();
	}
	
	public Cluster(Cluster pre, Cluster ponto, int index) {
		this.range = pre.getRange().intersection(ponto.getRange());
		ids = new TreeSet<Integer>(pre.getIds());
		ids.add(index);
		this.setOcorr(pre.getPrimOcorr());
		this.setOcorr(pre.getUltimaOcorr());
		this.setOcorr(ponto.getPrimOcorr());
		this.setOcorr(ponto.getUltimaOcorr());
	}
	
	public TreeSet<Integer> getIds() {
		return ids;
	}

	public void setIds(TreeSet<Integer> ids) {
		this.ids = ids;
	}

	public Range<Decimal> getRange() {
		return range;
	}
	public void setRange(Range<Decimal> range) {
		this.range = range;
	}

	public Date getPrimOcorr() {
		return primOcorr;
	}

	public int getPregao() {
		return pregao;
	}

	public void setPregao(int pregao) {
		this.pregao = pregao;
	}

	public Decimal getBase() {
		return base;
	}

	public void setBase(Decimal base) {
		this.base = base;
	}

	public void setPrimOcorr(Date primOcorr) {
		this.primOcorr = primOcorr;
	}

	public Date getUltimaOcorr() {
		return ultimaOcorr;
	}

	public void setUltimaOcorr(Date ultimaOcorr) {
		this.ultimaOcorr = ultimaOcorr;
	}

	public void setOcorr(Date ocorr) {
		if (ocorr != null) {
			if (primOcorr == null || ocorr.before(primOcorr)) {
				Date aux = primOcorr;
				primOcorr = ocorr;
				setOcorr(aux);
			} else if (ultimaOcorr == null || ocorr.after(ultimaOcorr)) {
				ultimaOcorr = ocorr;
			}
		}
	}

	public Boolean isMaxima() {
		return maxima;
	}

	public void setMaxima(Boolean maxima) {
		this.maxima = maxima;
	}

	public Decimal media() {
		return range.lowerEndpoint().plus(range.upperEndpoint()).dividedBy(Decimal.TWO);
	}
	
	public Cluster merge(Cluster outro) {
		Cluster res = this, alvo = outro;
		if (outro.ids.size() > this.ids.size()) {
			res = outro;
			alvo = this;
		}
		res.ids.addAll(alvo.ids);
		res.setOcorr(alvo.getPrimOcorr());
		res.setOcorr(alvo.getUltimaOcorr());

		return res;
	}
}
