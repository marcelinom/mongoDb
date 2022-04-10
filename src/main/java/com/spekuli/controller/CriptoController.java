package com.spekuli.controller;

import java.time.LocalDate;
import java.util.GregorianCalendar;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spekuli.exception.RegraNegocioException;
import com.spekuli.model.dto.AnaliseGrafica;
import com.spekuli.model.entity.Cripto;
import com.spekuli.service.impl.CalculoServiceImpl;
import com.spekuli.service.impl.CandleBinanceServiceImpl;
import com.spekuli.service.impl.CandleStreamBinanceServiceImpl;
import com.spekuli.service.impl.CriptoServiceImpl;
import com.spekuli.util.Interval;
import com.spekuli.util.Symbol;

import eu.verdelhan.ta4j.TimeSeries;

@RestController
@RequestMapping("/cripto")
public class CriptoController {
	
	@Autowired private CriptoServiceImpl cripto;
	@Autowired private CandleBinanceServiceImpl candle;
	@Autowired private CandleStreamBinanceServiceImpl stream;
	@Autowired private CalculoServiceImpl calculo;
	
	@GetMapping("/temporal/{codigo}/{timeframe}/{inicio}/{fim}")
	public ResponseEntity<AnaliseGrafica> buscaCripto(@PathVariable("codigo") String codigo, @PathVariable("timeframe") int timeFrame, @PathVariable("inicio") String inicio, @PathVariable("fim") String fim) {
    	String[] sInicio = inicio.split("-");
    	String[] sFim = fim.split("-");

    	TimeSeries series = cripto.buscaSerieTemporal(codigo.toUpperCase().trim(), 
    			new GregorianCalendar(Integer.valueOf(sInicio[0]),Integer.valueOf(sInicio[1])-1,Integer.valueOf(sInicio[2])).getTime(), 
    			new GregorianCalendar(Integer.valueOf(sFim[0]),Integer.valueOf(sFim[1])-1,Integer.valueOf(sFim[2])).getTime());
		
    	if (series != null && series.getTickCount()>0) {
        	AnaliseGrafica graf = calculo.painelAVistaDiario(series, timeFrame);
        	if (graf != null) return ResponseEntity.ok().body(graf);
    	}
    	
    	return ResponseEntity.ok().build();
	}
	
	@PutMapping("/extrair/{codigo}/{inicio}/{fim}")
	public ResponseEntity<AnaliseGrafica> extrairDados(@PathVariable("codigo") String codigo, @PathVariable("inicio") String inicio, @PathVariable("fim") String fim) {
    	candle.mineData(LocalDate.parse(inicio), LocalDate.parse(fim), Symbol.parse(codigo), Interval.ONE_DAY);
    	return ResponseEntity.ok().build();
	}
	
	@PostMapping("/stream/{fim}")
	public ResponseEntity<AnaliseGrafica> ativarStreamDados(@PathVariable("fim") String fim) {
    	stream.mineData(LocalDate.parse(fim), Interval.ONE_MIN);
    	return ResponseEntity.ok().build();
	}
	
	@PostMapping("/temporal/{codigo}/{hora}/{valor}")
	public ResponseEntity<Cripto> gravaCripto(@PathVariable("codigo") String codigo, @PathVariable("hora") String hora, @PathVariable("valor") String valor) throws RegraNegocioException {
		return ResponseEntity.ok().body(cripto.gravarCripto(codigo, hora, valor));
	}
	
	@GetMapping("/scalping/{codigo}/{timeframe}/{inicio}/{fim}")
	public ResponseEntity<AnaliseGrafica> obterDadosScalping(@PathVariable("codigo") String codigo, @PathVariable("timeframe") int timeFrame, @PathVariable("inicio") String inicio, @PathVariable("fim") String fim) throws NumberFormatException, RegraNegocioException {
    	String[] sInicio = inicio.split("-");
    	String[] sFim = fim.split("-");

		stream.mineData(LocalDate.parse(fim), Interval.ONE_MIN);
		
		TimeSeries series = cripto.buscaScalper(codigo.toUpperCase().trim(),
	    			new GregorianCalendar(Integer.valueOf(sInicio[0]),Integer.valueOf(sInicio[1])-1,Integer.valueOf(sInicio[2])).getTime(), 
	    			new GregorianCalendar(Integer.valueOf(sFim[0]),Integer.valueOf(sFim[1])-1,Integer.valueOf(sFim[2])).getTime());
    	
		if (series != null && series.getTickCount()>0) {
        	AnaliseGrafica graf = calculo.painelAVistaDiario(series, timeFrame);
        	if (graf != null) return ResponseEntity.ok().body(graf);
    	}
		
    	return ResponseEntity.ok().build();
	}	

	@GetMapping("/codigo/listar")
	public ResponseEntity<Set<String>> buscaCodigos(@RequestParam("term") String codigo) throws RegraNegocioException {
		return ResponseEntity.ok().body(cripto.buscaMoeda(codigo));
	}
	
}
