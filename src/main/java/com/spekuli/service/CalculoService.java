package com.spekuli.service;

import java.util.List;

import com.spekuli.model.dto.AnaliseGrafica;
import com.spekuli.model.dto.AnaliseNormal;
import com.spekuli.model.entity.Scalper;
import com.spekuli.util.Cluster;

import eu.verdelhan.ta4j.TimeSeries;

public interface CalculoService {

	AnaliseGrafica painelAVistaDiario(TimeSeries series, int timeFrame);
	AnaliseGrafica painelAVistaScalp(Scalper scalper, TimeSeries series, int timeFrame);
	List<AnaliseNormal> normaisAVistaDiario(TimeSeries series, List<Cluster> dias, int timeFrame);
		
}
