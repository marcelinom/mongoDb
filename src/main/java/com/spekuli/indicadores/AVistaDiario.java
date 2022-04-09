package com.spekuli.indicadores;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import com.google.common.collect.Range;
import com.spekuli.model.dto.Linha;
import com.spekuli.model.dto.PontoDV;
import com.spekuli.model.dto.Tripla;
import com.spekuli.model.entity.Gap;
import com.spekuli.util.Cluster;
import com.spekuli.util.ClusterByValor;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Indicator;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.helpers.GapIndicator;
import eu.verdelhan.ta4j.indicators.oscillators.StochasticOscillatorDIndicator;
import eu.verdelhan.ta4j.indicators.oscillators.StochasticOscillatorKIndicator;
import eu.verdelhan.ta4j.indicators.simple.AvgPriceIndicator;
import eu.verdelhan.ta4j.indicators.simple.MaxPriceIndicator;
import eu.verdelhan.ta4j.indicators.simple.MinPriceIndicator;
import eu.verdelhan.ta4j.indicators.statistics.StandardDeviationIndicator;
import eu.verdelhan.ta4j.indicators.trackers.EMAIndicator;
import eu.verdelhan.ta4j.indicators.trackers.MACDIndicator;
import eu.verdelhan.ta4j.indicators.trackers.RSIIndicator;
import eu.verdelhan.ta4j.indicators.trackers.bollinger.PercentBIndicator;
import eu.verdelhan.ta4j.indicators.volume.AccumulationDistributionIndicator;
import eu.verdelhan.ta4j.indicators.volume.CumulativeDeltaIndicator;
import eu.verdelhan.ta4j.indicators.volume.OnBalanceVolumeAvgIndicator;
import eu.verdelhan.ta4j.indicators.volume.OnBalanceVolumeIndicator;

public class AVistaDiario {
	public static int DIAS = 24*60*60*1000;
	public static int MINUTOS = 60*1000;
	public static Decimal MARGEM_MERGE = Decimal.valueOf(0.05);
	public static Decimal MARGEM_INF = Decimal.valueOf(0.10);
	public static Decimal MARGEM_SUP = Decimal.valueOf(1.0);
	public static int MINIMO_PREGOES_DEFAULT = 2;
	public static BigDecimal GANHO_DEFAULT = new BigDecimal(0.05);
	public static BigDecimal GANHO_SCALPING = new BigDecimal(0.001);
	public static double MIN_BBWIDTH = 0.8;

	public List<Cluster> calculaSuptResist(List<Cluster> intervalos) {
		// calcular todas possiveis superposicoes de intervalos 
		// combinar as interseccoes de todos n intervalos (em 2 a 2, 3 a 3,..., n a n) 
		List<Cluster> linhas = new ArrayList<Cluster>();
		for (int i=0; i<intervalos.size(); i++) {
			Cluster clt = intervalos.get(i);
			clt.getIds().add(i);
			buscaSuperposicoes(linhas, clt, intervalos, i+1);
		}
		
		Cluster[] SR = linhas.toArray(new Cluster[]{});
		Arrays.sort(SR, new ClusterByValor());
		return merge(Arrays.asList(SR), MARGEM_MERGE);
	}
		
	private List<Cluster> merge(List<Cluster> sr, Decimal margem) {
		List<Cluster> linhas = new ArrayList<Cluster>();
		for (int i=1; i<sr.size(); i++) {
			if (sr.get(i).media().minus(sr.get(i-1).media()).abs().isLessThanOrEqual(ajustaMargem(sr.get(i).media().multipliedBy(margem)))) {
				for (int j=0; j<sr.size(); j++) {
					if (j==i) {
						linhas.add(sr.get(i).merge(sr.get(i-1)));						
					} else if (j<i-1 || j>i) {
						linhas.add(sr.get(j));
					}
				}
				return merge(linhas, margem);
			};
		}
		return sr;
	}
	
	private Decimal ajustaMargem(Decimal margem) {
		return margem.max(MARGEM_INF).min(MARGEM_SUP);
	}
	
	public List<PontoDV> buscaGaps(Indicator<Decimal> indicador, int timeFrame, Decimal margem) {
        TimeSeries series = indicador.getTimeSeries();
        
		//coletar todos os pontos de gap
		int total = series.getTickCount();
        GapIndicator gap = new GapIndicator(indicador, timeFrame, margem);
        ArrayList<PontoDV> gaps = new ArrayList<PontoDV>();
		for (int i=0; i<total; i++) {
			if (!gap.getValue(i).equals(Decimal.ZERO)) {
				gaps.add(new PontoDV(series.getTick(i).getEndTime().toDate(), indicador.getValue(i).toDouble()));
			}
		}
		
		return gaps;
	}
	
	public List<Gap> buscaGaps(Indicator<Decimal> indicador, Decimal margem) {
        TimeSeries series = indicador.getTimeSeries();
        String acao = series.getName().split(",")[0];
        
		//coletar todos os pontos de gap
		int total = series.getTickCount();
        GapIndicator gap = new GapIndicator(indicador, 1, margem);
        ArrayList<Gap> gaps = new ArrayList<Gap>();
		for (int i=0; i<total; i++) {
			if (!gap.getValue(i).equals(Decimal.ZERO)) {
				gaps.add(new Gap(acao, series.getTick(i).getEndTime(), gap.getValue(i)));
			}
		}
		
		return gaps;
	}
	
	private int achaPontoExtremo(Indicator<Decimal> ind, boolean maximo, int primeiro, int ultimo) {
		Decimal extremo = null; 
		int index = (primeiro+ultimo)/2;
		for (int j=primeiro; j<ultimo; j++) {
			if (maximo) {
				if (extremo == null || ind.getValue(j).isGreaterThan(extremo)) {
					extremo = ind.getValue(j);
					index = j;
				}
			} else {
				if (extremo == null || ind.getValue(j).isLessThan(extremo)) {
					extremo = ind.getValue(j);
					index = j;
				}
			}
		}
		return index;
	}

	public List<Cluster> buscaMaxMin(Indicator<Decimal> ind, int timeFrame) {
        TimeSeries series = ind.getTimeSeries();
        ArrayList<Cluster> maxMin = new ArrayList<Cluster>();
        // calcular desvio nos valores a serem considerados
        StandardDeviationIndicator std = calculaDesvioMedio(series, 4*timeFrame);

        Boolean maximo = null;
        Double sinal, anterior = null;
        int total = series.getTickCount();
		for (int i=0; i<total; i++) {
			sinal = senoLinha(geraCurva(ind, 1, Math.max(0, i-timeFrame), Math.min(total-1, i+timeFrame)));
			if (anterior != null) {
				if (!sinal.equals(anterior)) {
					if (sinal>0 || (anterior<0 && sinal==0)) maximo = false;
					else maximo = true;
					int ultimo = maxMin.size()-1;
					if (ultimo<0 || !maximo.equals(maxMin.get(ultimo).isMaxima())) {
						int index = achaPontoExtremo(ind, maximo, Math.max(ultimo<0?0:maxMin.get(ultimo).getPregao()+1, i-timeFrame), Math.min(total-1, i+timeFrame));
						Decimal desvio = std.getValue(index);
						maxMin.add(new Cluster(index, maximo, ind.getValue(index), Range.closed(ind.getValue(index).minus(desvio), ind.getValue(index).plus(desvio)),series.getTick(index).getEndTime().toDate()));
						if (index > i) i = index;
					}
				}
			}
			anterior = sinal;
		}
		return maxMin;
	}

	public List<Cluster> buscaPicos(Indicator<Decimal> ind, PercentBIndicator pB, double min, double max) {
        TimeSeries series = ind.getTimeSeries();
        int total = series.getTickCount();
        ArrayList<Cluster> maxMin = new ArrayList<Cluster>();
        
        double flag, prev=0;
		for (int i=0; i<total;) {
			Integer index = null;
			do {
				flag = pB.getValue(i).toDouble();
				if (flag>max || flag<min) {
					index = i;
					prev = flag;
				}
			} while (++i<total && (flag>max || flag<min));
			if (index != null) maxMin.add(new Cluster(index, prev>max?true:false, ind.getValue(index), null, series.getTick(index).getEndTime().toDate()));
		}
		return maxMin;
	}

	private List<Cluster> buscaProximoCume(Decimal vale, List<Cluster> lista) {
		int cume = 0;
		Decimal ultVale = null;
		for (int i=1; lista != null && i<lista.size(); i++) {
			Cluster novo = lista.get(i);
			if (novo.isMaxima()) {
				if (novo.getBase().isGreaterThanOrEqual(lista.get(cume).getBase())) {
					if (ultVale != null && vale != null && vale.isGreaterThan(ultVale)) {
						break;	// cabeca sem ombro ou ombro caido demais
					} else {
						cume = i;
						vale = ultVale;
					}
				}
				else break;
			} else {
				ultVale = lista.get(i).getBase();
			}
		}
		
		if (cume == 0) return lista;
		else return lista.subList(cume, lista.size());
	}
	
	private List<Cluster> buscaProximoVale(Decimal cume, List<Cluster> lista) {
		int vale = 0;
		Decimal ultCume = null;
		for (int i=1; lista != null && i<lista.size(); i++) {
			Cluster novo = lista.get(i);
			if (!novo.isMaxima()) {
				if (novo.getBase().isLessThanOrEqual(lista.get(vale).getBase())) {
					if (ultCume != null && cume != null && cume.isLessThan(ultCume)) {
						break;		// cabeca sem ombro ou ombro caido demais
					} else {
						vale = i;
						cume = ultCume;
					}
				}
				else break;
			} else {
				ultCume = lista.get(i).getBase();
			}
		}
		
		if (vale == 0) return lista;
		else return lista.subList(vale, lista.size());
	}
	
	private List<Cluster> buscaProximo(Decimal anterior, List<Cluster> lista) {
    	if (lista.get(0).isMaxima()) return buscaProximoCume(anterior, lista);
    	else return buscaProximoVale(anterior, lista);
	}

	private List<Cluster> buscaProximoInverso(List<Cluster> lista, boolean maxima) {
    	for (int i=0; i<lista.size();i++) {
			if (lista.get(i).isMaxima() != maxima) {
				return buscaProximo(lista.get(0).getBase(), lista.subList(i, lista.size()));
			}
    	}
    	return lista;
	}

	public List<Cluster> buscaHeadAndShoulders(List<Cluster> maxMin) {
        ArrayList<Cluster> hs = new ArrayList<Cluster>();
        if (maxMin != null && maxMin.size()>1) {
            List<Cluster> sublista = buscaProximo(null, maxMin);
        	while (hs.size()==0 || hs.get(hs.size()-1).getPregao() < sublista.get(0).getPregao()) {
            	hs.add(sublista.get(0));
            	sublista = buscaProximoInverso(sublista, sublista.get(0).isMaxima());
        	}
        }
 
        return hs;
	}
	
	private StandardDeviationIndicator calculaDesvioMedio(TimeSeries series, int timeFrame) {
		List<Tick> ticks = new ArrayList<Tick>();
		for (Tick tick : series.getTicks()) {
			ticks.add(new Tick(tick.getEndTime(), 
					null, null, null, null,
					tick.getAvgPrice().minus(tick.getClosePrice()).abs().plus(tick.getAvgPrice().minus(tick.getOpenPrice()).abs()).dividedBy(Decimal.TWO), 
					null, null, 0));
		}
		TimeSeries aux = new TimeSeries(null, ticks);
		return new StandardDeviationIndicator(new AvgPriceIndicator(aux), timeFrame);
	}

	public Decimal calculaMaxVolInf(TimeSeries series, int inicio, int fim) {
		Decimal maxVol = Decimal.ZERO;
		if (inicio >= 0 && fim >= inicio) {
			Decimal compra = series.getTick(inicio).getAvgPrice();
			for (int i=inicio+1; i<=fim; i++) {
				Tick tick = series.getTick(i);
				if (compra.isGreaterThan(tick.getMinPrice())) {
					Decimal vol = compra.dividedBy(tick.getMinPrice()).minus(Decimal.ONE);
					if (vol.isGreaterThan(maxVol)) maxVol = vol;
				}
			}
		}
		return maxVol;
	}

	private void buscaSuperposicoes(List<Cluster> sr, Cluster alvo, List<Cluster> swings, int primeiro) {
		for (int i=primeiro; i<swings.size(); i++) {
			if (alvo.getRange().isConnected(swings.get(i).getRange())) {
				Cluster res = new Cluster(alvo, swings.get(i), i);
				if (!res.getRange().isEmpty()) {
					incluiCluster(sr, res);
					buscaSuperposicoes(sr, res, swings, i+1);
				}
			}
		}	
	}
	
	private void incluiCluster(List<Cluster> lista, Cluster candidato) {
		for (int i=0; i<lista.size(); i++) {
			if (candidato.getIds().containsAll(lista.get(i).getIds())) {
				lista.remove(i);
				break;
			} else if (lista.get(i).getIds().containsAll(candidato.getIds())) {
				return;
			}
		}
		lista.add(candidato);
	}
	
	public double senoLinha(double[] coeff) {
		if (coeff == null || coeff[1] == 0) {
			return 0;
		} else {
			double x1 = 0;
			double y1 = coeff[0];
			double y2 = 0;
			double x2 = -coeff[0]/coeff[1];
			
			if (x2 > x1) {
				return (y2-y1)/(Math.sqrt(Math.pow(y2-y1,2)+Math.pow(x2-x1, 2)));
			} else {
				return (y1-y2)/(Math.sqrt(Math.pow(y1-y2,2)+Math.pow(x1-x2, 2)));
			}
		}
	}
	
	public Vector2D cruzamentoLinhas(double[] cBaixa, double[] cAlta) {		
		double x, y, div = cBaixa[1]-cAlta[1];
		if (div != 0) {
			x = (cAlta[0]-cBaixa[0])/div;
			y = cAlta[1]*x + cAlta[0];			
			return new Vector2D(x,y);
		} else {
			return null;
		}
	}
	
	public List<Linha> geraLinhasTendencia(List<Cluster> intervalos, TimeSeries series) {
		int per = series.getTick(0).getTimePeriod().getDays()==0?MINUTOS:DIAS;
        List<Linha> linhas = new ArrayList<Linha>();
        //inclui ultimo tick da serie como head and shoulder 
        intervalos.add(new Cluster(series.getTickCount()-1));
        for (int i=0; i+1<intervalos.size(); i++) {
        	int primeiro = intervalos.get(i).getPregao(), ultimo = intervalos.get(i+1).getPregao();
        	if (primeiro != ultimo) {
        		double inicio = series.getTick(primeiro).getEndTime().getMillis()/per;
        		double[] a = geraLinhaTendenciaAlta(series, primeiro, ultimo);
        		linhas.add(new Linha(new PontoDV(series.getTick(primeiro).getEndTime().toDate(), (series.getTick(primeiro).getEndTime().getMillis()/per-inicio)*a[1]+a[0]), 
    		 			 			 new PontoDV(series.getTick(ultimo).getEndTime().toDate(), (series.getTick(ultimo).getEndTime().getMillis()/per-inicio)*a[1]+a[0])));
        		double[] b = geraLinhaTendenciaBaixa(series, primeiro, ultimo);
        		linhas.add(new Linha(new PontoDV(series.getTick(primeiro).getEndTime().toDate(), (series.getTick(primeiro).getEndTime().getMillis()/per-inicio)*b[1]+b[0]), 
    					 			 new PontoDV(series.getTick(ultimo).getEndTime().toDate(), (series.getTick(ultimo).getEndTime().getMillis()/per-inicio)*b[1]+b[0])));
        	}
        }
        //remove tick inserido
        intervalos.remove(intervalos.size()-1);
        
		return linhas;
	}
	
	public double[] geraLinhaTendenciaBaixa(TimeSeries series, int primeiro, int ultimo) {
        MaxPriceIndicator hpi = new MaxPriceIndicator(series);
        return geraCurva(hpi, 1, primeiro, ultimo);
	}
	
	public double[] geraLinhaTendenciaAlta(TimeSeries series, int primeiro, int ultimo) {
		MinPriceIndicator lpi = new MinPriceIndicator(series);
        return geraCurva(lpi, 1, primeiro, ultimo);
	}
	
	private double[] geraCurva(Double[] valores, int grau, int primeiro, int ultimo) {
		if (primeiro < ultimo) {
			double media = 0;
			for (int i=primeiro; i<=ultimo && i<valores.length; i++) {
				media += Math.abs(valores[i]);
			}
			media /= ultimo-primeiro+1;
			media = Math.max(2, Math.abs(media));

			WeightedObservedPoints obs = new WeightedObservedPoints();
			for (int i=primeiro; i<=ultimo && i<valores.length; i++) {
				obs.add(i*media*0.5, valores[i]);
			}
			
			PolynomialCurveFitter fitter = PolynomialCurveFitter.create(grau);
			try {
				return fitter.fit(obs.toList());			
			} catch(NotStrictlyPositiveException ne) {}
		}
		return null;
	}
	
	private double[] geraCurva(Indicator<?> indicador, int grau, int primeiro, int ultimo) {
		int per = indicador.getTimeSeries().getTick(primeiro).getTimePeriod().getDays()==0?MINUTOS:DIAS;
		double inicio = indicador.getTimeSeries().getTick(primeiro).getEndTime().getMillis()/per;
		WeightedObservedPoints obs = new WeightedObservedPoints();
		for (int i=primeiro; i<=ultimo && i<indicador.getTimeSeries().getTickCount(); i++) {
			obs.add(indicador.getTimeSeries().getTick(i).getEndTime().getMillis()/per-inicio, ((Decimal)indicador.getValue(i)).toDouble());
		}

		PolynomialCurveFitter fitter = PolynomialCurveFitter.create(grau);
		return fitter.fit(obs.toList());			
	}
		
	public List<PontoDV> calculaADLine(TimeSeries series) {
		AccumulationDistributionIndicator adl = new AccumulationDistributionIndicator(series);
		List<PontoDV> lista = new ArrayList<PontoDV>();
		for (int i=0; i<series.getTickCount(); i++) {
			lista.add(new PontoDV(series.getTick(i).getEndTime().toDate(), adl.getValue(i).toDouble()));
		}
		
		return lista;
	}
	
	public Double[] valoresADLine(TimeSeries series, Date dia, AccumulationDistributionIndicator adl, int timeFrame) {
		Double[] valores = new Double[2*timeFrame+1]; 		
		Arrays.fill(valores, Double.NaN);
		for (int i = 0; i<series.getTickCount(); i++) {
			if (dia.equals(series.getTick(i).getEndTime().toDate())) {
				if (i-timeFrame>=0) {
					for (int j=i-timeFrame; j<series.getTickCount() && j<=i+timeFrame; j++)
						valores[timeFrame+j-i] = adl.getValue(j).toDouble();
					return valores;
				} else return null;
			}
		}
		return null;		
	}
	
	public Double[] valoresIndicador(int pregao, Indicator<Decimal> ind, int timeFrame) {		
		if (pregao>timeFrame-2) {
			Double[] valores = new Double[timeFrame]; 		
			for (int i = pregao-timeFrame+1; i<=pregao; i++) {
				valores[i-pregao+timeFrame-1] = ind.getValue(i).toDouble();
			}
			return valores;
		}
		return null;
	}	
	
	public Double[] valoresMACD(int pregao, TimeSeries series, MACDIndicator macd, EMAIndicator ema, int timeFrame) {		
		Double[] valores = new Double[2*timeFrame+1]; 		
		Arrays.fill(valores, Double.NaN);
		if (pregao-timeFrame>=0) {
			valores[timeFrame] = macd.getValue(pregao).toDouble() - ema.getValue(pregao).toDouble();
			for (int j=pregao-timeFrame; j<pregao; j++)
				valores[timeFrame+j-pregao] = macd.getValue(j).toDouble() - ema.getValue(j).toDouble();
			for (int j=pregao+1; j<series.getTickCount() && j<=pregao+timeFrame; j++)
				valores[timeFrame+j-pregao] = macd.getValue(j).toDouble() - ema.getValue(j).toDouble();
			return valores;
		} else return null;
	}	
	
	public Double dicaNormalStk(int pregao, TimeSeries series, StochasticOscillatorKIndicator stk, StochasticOscillatorDIndicator std) {				
		// res abaixo de 20 (acima de 0.6) indica comprar e acima de 80 (abaixo de -0.6), vender
		return -(std.getValue(pregao).toDouble()/100 * 2 -1);
	}	
	
	public Double dicaNormalRSI(int pregao, RSIIndicator rsi, TimeSeries series) {		
		// res abaixo de 30 (acima de 0.4) indica comprar e acima de 70 (abaixo de -0.4), vender
		return -(rsi.getValue(pregao).toDouble()/100 * 2 -1);
	}	
	
	public Double[] valoresDeltaR(int pregao, Indicator<Decimal> dma, int timeFrame) {	
		int total = dma.getTimeSeries().getTickCount();
		Double[] valores = new Double[2*timeFrame+1]; 		
		Arrays.fill(valores, Double.NaN);
		if (pregao-timeFrame>=0) {
			valores[timeFrame] = dma.getValue(pregao).toDouble();
			for (int j=pregao-timeFrame; j<pregao; j++)
				valores[timeFrame+j-pregao] = dma.getValue(j).toDouble();
			for (int j=pregao+1; j<total && j<=pregao+timeFrame; j++)
				valores[timeFrame+j-pregao] = dma.getValue(j).toDouble();
			return valores;
		} else return null;
	}	
	
	public Double dicaNormalAvancadaRSI(int pregao, RSIIndicator rsi, TimeSeries series, int timeFrame) {		
		Double valor = 0.0;
		for (int i = pregao; i<series.getTickCount() && i<=pregao+timeFrame; i++) {
			valor = dicaNormalRSI(i, rsi, series);
		}
		
		return valor;
	}	
	
	public Double dicaNormal(Double[] valores) {
		if (valores != null) {
			//negativo: tendencia de queda (vender); positivo: tendencia de alta (comprar)
			double[] res = geraCurva(valores, 1, 0, valores.length-1);	
			if (res != null) return senoLinha(res);		
		}
		return 0.0;
	}	
	
	public Double dicaNormalSimples(Double[] valores) {
		if (valores != null) {
			int ultimo = 0;
			while (ultimo<valores.length) {
				if (valores[ultimo].isNaN()) break;
				else ultimo++;
			}
			//negativo: tendencia de queda (vender); positivo: tendencia de alta (comprar)
			double[] res = geraCurva(valores, 1, 0, ultimo-1);	
			if (res != null) return senoLinha(res);		
			else return 0.0;
		}
		
		return null;
	}	
	
	public List<Tripla> geraEstocastico(TimeSeries series, int timeFrame) {
		StochasticOscillatorKIndicator stk = new StochasticOscillatorKIndicator(series, timeFrame);
		StochasticOscillatorDIndicator std = new StochasticOscillatorDIndicator(stk);
		List<Tripla> lista = new ArrayList<Tripla>();
		for (int i=0; i<series.getTickCount(); i++) {
			lista.add(new Tripla(series.getTick(i).getEndTime().toDate(), std.getValue(i).toDouble(), stk.getValue(i).toDouble()));
		}
		
		return lista;
	}
	
	public List<PontoDV> calculaRSI(Indicator<Decimal> indicador, int timeFrame) {
		TimeSeries series = indicador.getTimeSeries();
		RSIIndicator rsi = new RSIIndicator(indicador, timeFrame);
		List<PontoDV> lista = new ArrayList<PontoDV>();
		for (int i=timeFrame-1; i<series.getTickCount(); i++) {
			lista.add(new PontoDV(series.getTick(i).getEndTime().toDate(), rsi.getValue(i).toDouble()));
		}
		
		return lista;
	}
	
	public List<Tripla> calculaMACD(Indicator<Decimal> indicador, int shortTimeFrame, int longTimeFrame, int signalTimeFrame) {
		TimeSeries series = indicador.getTimeSeries();
		MACDIndicator macd = new MACDIndicator(indicador, shortTimeFrame, longTimeFrame);
		EMAIndicator ema = new EMAIndicator(macd, signalTimeFrame);
		List<Tripla> lista = new ArrayList<Tripla>();
		for (int i=longTimeFrame-1; i<series.getTickCount(); i++) {
			lista.add(new Tripla(series.getTick(i).getEndTime().toDate(), macd.getValue(i).toDouble(), ema.getValue(i).toDouble()));
		}
		
		return lista;
	}
			
	public List<PontoDV> calculaOBV(TimeSeries series) {
		OnBalanceVolumeIndicator obv = new OnBalanceVolumeIndicator(series);
		List<PontoDV> lista = new ArrayList<PontoDV>();
		for (int i=0; i<series.getTickCount(); i++) {
			lista.add(new PontoDV(series.getTick(i).getEndTime().toDate(), obv.getValue(i).toDouble()));
		}
		
		return lista;
	}
	
	public List<PontoDV> calculaCumDelta(TimeSeries series) {
		EMAIndicator ecd = new EMAIndicator(new CumulativeDeltaIndicator(series), 4);
		List<PontoDV> lista = new ArrayList<PontoDV>();
		for (int i=0; i<series.getTickCount(); i++) {
			lista.add(new PontoDV(series.getTick(i).getEndTime().toDate(), ecd.getValue(i).toDouble()));
		}
		
		return lista;
	}
	
	public Double[] valoresOBV(int pregao, TimeSeries series, OnBalanceVolumeAvgIndicator obv, int timeFrame) {		
		Double[] valores = new Double[2*timeFrame+1]; 		
		Arrays.fill(valores, Double.NaN);
		if (pregao-2*timeFrame>=0) {
			for (int j=pregao-2*timeFrame; j<series.getTickCount() && j<=pregao; j++)
				valores[2*timeFrame+j-pregao] = obv.getValue(j).toDouble();
			return valores;
		} else return null;

	}	
			
	public Double[] valoresCumDelta(int pregao, TimeSeries series, EMAIndicator cdi, int timeFrame) {		
		Double[] valores = new Double[2*timeFrame+1]; 		
		Arrays.fill(valores, Double.NaN);
		if (pregao-2*timeFrame>=0) {
			for (int j=pregao-2*timeFrame; j<series.getTickCount() && j<=pregao; j++)
				valores[2*timeFrame+j-pregao] = cdi.getValue(j).toDouble();
			return valores;
		} else return null;

	}	
			
}
