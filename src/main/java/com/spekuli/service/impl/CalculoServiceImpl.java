package com.spekuli.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.spekuli.indicadores.AVistaDiario;
import com.spekuli.model.dto.AnaliseGrafica;
import com.spekuli.model.dto.AnaliseNormal;
import com.spekuli.model.dto.Bollinger;
import com.spekuli.model.dto.CompraVende;
import com.spekuli.model.dto.Cotacao;
import com.spekuli.model.dto.Linha;
import com.spekuli.model.dto.PontoDV;
import com.spekuli.model.entity.Scalper;
import com.spekuli.service.CalculoService;
import com.spekuli.util.Cluster;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Indicator;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.oscillators.StochasticOscillatorDIndicator;
import eu.verdelhan.ta4j.indicators.oscillators.StochasticOscillatorKIndicator;
import eu.verdelhan.ta4j.indicators.simple.AvgPriceIndicator;
import eu.verdelhan.ta4j.indicators.simple.DeltaRelativoIndicator;
import eu.verdelhan.ta4j.indicators.statistics.StandardDeviationIndicator;
import eu.verdelhan.ta4j.indicators.trackers.EMAIndicator;
import eu.verdelhan.ta4j.indicators.trackers.MACDIndicator;
import eu.verdelhan.ta4j.indicators.trackers.RSIIndicator;
import eu.verdelhan.ta4j.indicators.trackers.SMAIndicator;
import eu.verdelhan.ta4j.indicators.trackers.bollinger.BollingerBandsLowerIndicator;
import eu.verdelhan.ta4j.indicators.trackers.bollinger.BollingerBandsMiddleIndicator;
import eu.verdelhan.ta4j.indicators.trackers.bollinger.BollingerBandsUpperIndicator;
import eu.verdelhan.ta4j.indicators.volume.OnBalanceVolumeAvgIndicator;

@Service
public class CalculoServiceImpl implements CalculoService {

	public AnaliseGrafica painelAVistaDiario(TimeSeries series, int timeFrame) {
		AVistaDiario avd = new AVistaDiario();
		AnaliseGrafica analise = new AnaliseGrafica(series.getName().split(",")[1]);
		ArrayList<Cotacao> cotacoes = new ArrayList<Cotacao>();
		for (Tick tick : series.getTicks()) {
			cotacoes.add(new Cotacao(tick.getEndTime().toDate(), tick.getOpenPrice(), tick.getClosePrice(), tick.getMaxPrice(), tick.getMinPrice(),tick.getAvgPrice() ,tick.getVolume()));
		}
		analise.setCotacoes(cotacoes);
		
		AvgPriceIndicator avgPrice = new AvgPriceIndicator(series);
        List<Cluster> picos = avd.buscaMaxMin(avgPrice, timeFrame);
		List<Cluster> linhas = avd.calculaSuptResist(picos);
		ArrayList<Linha> supstances = new ArrayList<Linha>();
		for (Cluster cl : linhas) {
			supstances.add(new Linha(new PontoDV(cl.getPrimOcorr(), cl.media().toDouble()), new PontoDV(cl.getUltimaOcorr(), cl.media().toDouble())));
		}
		analise.setSupstances(supstances);
		
		analise.setTrends(avd.geraLinhasTendencia(picos, series));
		analise.setTrendsG(avd.geraLinhasTendencia(avd.buscaMaxMin(avgPrice, timeFrame*5), series));

		// Simple moving averages
		Indicator<Decimal> sma = new SMAIndicator(avgPrice, 20);
		List<PontoDV> lsma = new ArrayList<PontoDV>();
		for (int i=19; i<sma.getTimeSeries().getTickCount(); i++) {
			lsma.add(new PontoDV(sma.getTimeSeries().getTick(i).getEndTime().toDate(), sma.getValue(i).toDouble()));
		}
		analise.setSma20(lsma);

		Indicator<Decimal> sma2 = new SMAIndicator(avgPrice, 50);
		List<PontoDV> lsma2 = new ArrayList<PontoDV>();
		for (int i=49; i<sma2.getTimeSeries().getTickCount(); i++) {
			lsma2.add(new PontoDV(sma2.getTimeSeries().getTick(i).getEndTime().toDate(), sma2.getValue(i).toDouble()));
		}
		analise.setSma50(lsma2);

		Indicator<Decimal> sma3 = new SMAIndicator(avgPrice, 200);
		List<PontoDV> lsma3 = new ArrayList<PontoDV>();
		for (int i=199; i<sma3.getTimeSeries().getTickCount(); i++) {
			lsma3.add(new PontoDV(sma3.getTimeSeries().getTick(i).getEndTime().toDate(), sma3.getValue(i).toDouble()));
		}
		analise.setSma200(lsma3);

        // Bollinger bands
		StandardDeviationIndicator std = new StandardDeviationIndicator(avgPrice, 20);
        BollingerBandsMiddleIndicator middleBBand = new BollingerBandsMiddleIndicator(sma);
        BollingerBandsLowerIndicator lowerBBand = new BollingerBandsLowerIndicator(middleBBand, std);
        BollingerBandsUpperIndicator upperBBand = new BollingerBandsUpperIndicator(middleBBand, std);
		List<Bollinger> bollinger = new ArrayList<Bollinger>();
		for (int i=19; i<middleBBand.getTimeSeries().getTickCount(); i++) {
			bollinger.add(new Bollinger(middleBBand.getTimeSeries().getTick(i).getEndTime().toDate(), lowerBBand.getValue(i).toDouble(), middleBBand.getValue(i).toDouble(), upperBBand.getValue(i).toDouble()));
		}
		analise.setBollinger(bollinger);

		//dia com variacao maior ou igual a 5% entre D e D+1
		analise.setGaps(avd.buscaGaps(avgPrice, 1, Decimal.valueOf(0.05)));
		
		analise.setStk(avd.geraEstocastico(series, 14));
		analise.setRsi(avd.calculaRSI(avgPrice, 14));
		analise.setMacd(avd.calculaMACD(avgPrice, 12, 26, 9));
		analise.setObv(avd.calculaOBV(series));

		return analise;
	}
	
	public AnaliseGrafica painelAVistaScalp(Scalper scalper, TimeSeries series, int timeFrame) {
		AVistaDiario avd = new AVistaDiario();
		AnaliseGrafica analise = new AnaliseGrafica(series.getName().split(",")[1]);
		ArrayList<Cotacao> cotacoes = new ArrayList<Cotacao>();
		for (Tick tick : series.getTicks()) {
			cotacoes.add(new Cotacao(tick.getEndTime().toDate(), tick.getOpenPrice(), tick.getClosePrice(), tick.getMaxPrice(), tick.getMinPrice(),tick.getAvgPrice() ,tick.getVolume()));
		}
		analise.setCotacoes(cotacoes);
		
		AvgPriceIndicator avgPrice = scalper==null?new AvgPriceIndicator(series):scalper.getAvgPrice();
        List<Cluster> picos = avd.buscaMaxMin(avgPrice, timeFrame);
		analise.setTrends(avd.geraLinhasTendencia(picos, series));
		analise.setTrendsG(avd.geraLinhasTendencia(avd.buscaMaxMin(avgPrice, (int)(timeFrame*4.236)), series));
        		
		// Exponential moving averages
		Indicator<Decimal> ema = new EMAIndicator(avgPrice, 4);
		List<PontoDV> lema = new ArrayList<PontoDV>();
		for (int i=0; i<ema.getTimeSeries().getTickCount(); i++) {
			lema.add(new PontoDV(ema.getTimeSeries().getTick(i).getEndTime().toDate(), ema.getValue(i).toDouble()));
		}
		analise.setSma20(lema);

		Indicator<Decimal> ema2 = new EMAIndicator(avgPrice, 18);
		List<PontoDV> lema2 = new ArrayList<PontoDV>();
		for (int i=0; i<ema2.getTimeSeries().getTickCount(); i++) {
			lema2.add(new PontoDV(ema2.getTimeSeries().getTick(i).getEndTime().toDate(), ema2.getValue(i).toDouble()));
		}
		analise.setSma50(lema2);

		Indicator<Decimal> ema3 = new EMAIndicator(avgPrice, 76);
		List<PontoDV> lema3 = new ArrayList<PontoDV>();
		for (int i=0; i<ema3.getTimeSeries().getTickCount(); i++) {
			lema3.add(new PontoDV(ema3.getTimeSeries().getTick(i).getEndTime().toDate(), ema3.getValue(i).toDouble()));
		}
		analise.setSma200(lema3);

        // Bollinger bands
		Indicator<Decimal> sma = new SMAIndicator(avgPrice, 18);
		StandardDeviationIndicator std = new StandardDeviationIndicator(avgPrice, 18);
        BollingerBandsMiddleIndicator middleBBand = new BollingerBandsMiddleIndicator(sma);
        BollingerBandsLowerIndicator lowerBBand = new BollingerBandsLowerIndicator(middleBBand, std);
        BollingerBandsUpperIndicator upperBBand = new BollingerBandsUpperIndicator(middleBBand, std);
		List<Bollinger> bollinger = new ArrayList<Bollinger>();
		for (int i=1; i<middleBBand.getTimeSeries().getTickCount(); i++) {
			bollinger.add(new Bollinger(middleBBand.getTimeSeries().getTick(i).getEndTime().toDate(), lowerBBand.getValue(i).toDouble(), middleBBand.getValue(i).toDouble(), upperBBand.getValue(i).toDouble()));
		}
		analise.setBollinger(bollinger);

		//dia com variacao maior ou igual a 5% entre D e D+1
		analise.setGaps(avd.buscaGaps(avgPrice, 1, Decimal.valueOf(0.005)));
		analise.setStk(avd.geraEstocastico(series, 18));
		
		Indicator<Decimal> dma = scalper==null?new EMAIndicator(new DeltaRelativoIndicator(series), 18):scalper.getDma();
		List<PontoDV> drel = new ArrayList<PontoDV>();
		for (int i=0; i<series.getTickCount(); i++) {
			drel.add(new PontoDV(series.getTick(i).getEndTime().toDate(), dma.getValue(i).toDouble()*100));
		}
		analise.setRsi(drel);
		analise.setMacd(avd.calculaMACD(avgPrice, 12, 26, 9));
		analise.setObv(avd.calculaCumDelta(series));

		analise.setQuando(new ArrayList<CompraVende>());
				
		return analise;
	}
	
	public List<AnaliseNormal> normaisAVistaDiario(TimeSeries series, List<Cluster> dias, int timeFrame) {
		AvgPriceIndicator avgPrice = new AvgPriceIndicator(series);
		RSIIndicator rsi = new RSIIndicator(avgPrice, 14);
		
		StochasticOscillatorKIndicator stk = new StochasticOscillatorKIndicator(series, 14);
		StochasticOscillatorDIndicator stkd = new StochasticOscillatorDIndicator(stk);

		OnBalanceVolumeAvgIndicator obv = new OnBalanceVolumeAvgIndicator(series);

		MACDIndicator macd = new MACDIndicator(avgPrice, 12, 26);
		EMAIndicator ema = new EMAIndicator(macd, 9);

		AVistaDiario avd = new AVistaDiario();
		ArrayList<AnaliseNormal> normais = new ArrayList<AnaliseNormal>();
		for (int i=0; i<dias.size(); i++) {
			int pregao = dias.get(i).getPregao();
			if (pregao>26) {
				normais.add(new AnaliseNormal(dias.get(i).getPrimOcorr(),
						0.0, 
						avd.dicaNormalRSI(pregao, rsi, series), 
						avd.dicaNormalSimples(avd.valoresMACD(pregao, series, macd, ema, timeFrame)), 
						avd.dicaNormalStk(pregao, series, stk, stkd), 
						avd.dicaNormalSimples(avd.valoresOBV(pregao, series, obv, timeFrame)),
						avd.dicaNormalAvancadaRSI(pregao, rsi, series, timeFrame),
						0.0));						
			}
		}
		
		return normais;
	}	
		
}
