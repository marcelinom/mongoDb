package com.spekuli.service;


import java.util.Date;
import java.util.Set;

import com.spekuli.exception.RegraNegocioException;
import com.spekuli.model.entity.Cripto;

import eu.verdelhan.ta4j.TimeSeries;

public interface CriptoService {

	TimeSeries buscaScalper(String codigo, Date inicio, Date fim) throws RegraNegocioException;
	TimeSeries buscaSerieTemporal(String codigo, Date inicio, Date fim);
	void gravarGaps(String codigo, Date inicio, Date fim) throws RegraNegocioException;
	Cripto gravarCripto(String codigo, String hora, String valor) throws RegraNegocioException;
	Set<String> buscaMoeda(String codigo) throws RegraNegocioException;
	
}
