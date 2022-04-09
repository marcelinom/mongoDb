package com.spekuli.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spekuli.exception.RegraNegocioException;
import com.spekuli.indicadores.AVistaDiario;
import com.spekuli.model.dao.CriptoCustomRepository;
import com.spekuli.model.dao.GapRepository;
import com.spekuli.model.dao.ScalperCustomRepository;
import com.spekuli.model.entity.Cripto;
import com.spekuli.model.entity.Gap;
import com.spekuli.model.entity.Scalper;
import com.spekuli.service.CriptoService;
import com.spekuli.util.Symbol;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.simple.AvgPriceIndicator;

@Service
public class CriptoServiceImpl implements CriptoService {
	@Autowired private CriptoCustomRepository histRepo;
	@Autowired private ScalperCustomRepository snapRepo;
	@Autowired private GapRepository gapRepo;

	public TimeSeries buscaScalper(String codigo, Date inicio, Date fim) throws RegraNegocioException {
		List<Scalper> lista = snapRepo.listByCodigoPeriod(codigo, inicio, fim);

		TimeSeries serie = new TimeSeries(codigo+","+codigo);
		for (Scalper scalper : lista) {
			serie.addTick(new Tick(
					1,
					new DateTime(scalper.getData()), 
	           		Decimal.valueOf(scalper.getOpen()), 
	           		Decimal.valueOf(scalper.getHigh()), 
	           		Decimal.valueOf(scalper.getLow()), 
	           		Decimal.valueOf(scalper.getClose()), 
	           		Decimal.valueOf(scalper.calculaVwap()), 
	           		Decimal.valueOf(scalper.getVolume()),
	           		Decimal.valueOf(0),
	           		scalper.getNegocios(),
	           		0));
		}
		
		return serie;
	}
	
	public TimeSeries buscaSerieTemporal(String codigo, Date inicio, Date fim) {
		List<Tick> ticks = new ArrayList<Tick>();
		List<Cripto> lista = histRepo.listByCodigoPeriod(codigo, inicio, fim);
		
		if (lista != null) {
			for (Cripto acao : lista) {
	            ticks.add(new Tick(new DateTime(java.sql.Date.valueOf(acao.getPeriodo())), 
		           		Decimal.valueOf(acao.getAbertura()), 
		           		Decimal.valueOf(acao.getMaximo()), 
		           		Decimal.valueOf(acao.getMinimo()), 
		           		Decimal.valueOf(acao.getUltimo()), 
		           		Decimal.valueOf(acao.getUltimo()), 
		           		Decimal.valueOf(acao.getVolume()),
		           		Decimal.valueOf(0),
		           		acao.getNegocios().intValue()));
	        }
			String nome = lista.size()>0?lista.get(0).getNome():"-";
			return new TimeSeries(codigo+","+nome, ticks);
		}
		
		return null;
	}
	
	public void gravarGaps(String codigo, Date inicio, Date fim) throws RegraNegocioException {
		TimeSeries series = buscaSerieTemporal(codigo, inicio, fim);
		AvgPriceIndicator avgPrice = new AvgPriceIndicator(series);
        List<Gap> gaps= new AVistaDiario().buscaGaps(avgPrice, Decimal.valueOf(AVistaDiario.GANHO_DEFAULT));
        		
        gapRepo.deleteAll();
		for (Gap gap : gaps) {
			gapRepo.save(gap);
		}
	}

	@Override
	public Cripto gravarCripto(String codigo, String dia, String valor) throws RegraNegocioException {
		Cripto coin = Cripto.builder()
                .codigo(codigo)
                .periodo(LocalDate.parse(dia))
                .abertura(new BigDecimal(valor))
                .maximo(new BigDecimal(valor))
                .minimo(new BigDecimal(valor))
                .ultimo(new BigDecimal(valor))
                .volume(new BigDecimal(30000))
                .negocios(Long.parseLong("9580"))
                .build();
				
		return histRepo.save(coin);
	}

	@Override
	public Set<String> buscaMoeda(String codigo) throws RegraNegocioException {
		TreeSet<String> lista = new TreeSet<String>();
		for (Symbol coin : Symbol.values()) {
			lista.add(coin.getCode());
		}
		
		return lista;
	}
	
}
