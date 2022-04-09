package com.spekuli.model.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import eu.verdelhan.ta4j.Decimal;

public class Cotacao implements Serializable {
	private static final long serialVersionUID = -8572586450338795499L;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	private String data;
	private double abertura;
	private double fechamento;
	private double maxima;
	private double media;
	private double minima;
	private double volume;
	
	public Cotacao(Date data, Decimal abertura, Decimal fechamento, Decimal maxima, Decimal minima, Decimal media, Decimal volume) {
		setData(data);
		setAbertura(abertura);
		setFechamento(fechamento);
		setMaxima(maxima);
		setMedia(media);
		setMinima(minima);
		setVolume(volume);
	}
	public String getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = sdf.format(data);
	}
	public double getAbertura() {
		return abertura;
	}
	public void setAbertura(Decimal abertura) {
		this.abertura = abertura.toDouble();
	}
	public double getFechamento() {
		return fechamento;
	}
	public void setFechamento(Decimal fechamento) {
		this.fechamento = fechamento.toDouble();
	}
	public double getMedia() {
		return media;
	}
	public void setMedia(Decimal media) {
		this.media = media==null?0:media.toDouble();
	}
	public double getMaxima() {
		return maxima;
	}
	public void setMaxima(Decimal maxima) {
		this.maxima = maxima.toDouble();
	}
	public double getMinima() {
		return minima;
	}
	public void setMinima(Decimal minima) {
		this.minima = minima.toDouble();
	}
	public double getVolume() {
		return volume;
	}
	public void setVolume(Decimal volume) {
		this.volume = volume.toDouble();
	}
	
	
}
