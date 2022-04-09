package com.spekuli.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.simple.AvgPriceIndicator;
import eu.verdelhan.ta4j.indicators.simple.DeltaRelativoIndicator;
import eu.verdelhan.ta4j.indicators.simple.MaxPriceIndicator;
import eu.verdelhan.ta4j.indicators.simple.MinPriceIndicator;
import eu.verdelhan.ta4j.indicators.statistics.StandardDeviationIndicator;
import eu.verdelhan.ta4j.indicators.trackers.EMAIndicator;
import eu.verdelhan.ta4j.indicators.trackers.SMAIndicator;
import eu.verdelhan.ta4j.indicators.trackers.bollinger.BollingerBandWidthIndicator;
import eu.verdelhan.ta4j.indicators.trackers.bollinger.BollingerBandsLowerIndicator;
import eu.verdelhan.ta4j.indicators.trackers.bollinger.BollingerBandsMiddleIndicator;
import eu.verdelhan.ta4j.indicators.trackers.bollinger.BollingerBandsUpperIndicator;
import eu.verdelhan.ta4j.indicators.trackers.bollinger.NegativePercentBIndicator;
import eu.verdelhan.ta4j.indicators.trackers.bollinger.PercentBIndicator;
import eu.verdelhan.ta4j.indicators.trackers.bollinger.PositivePercentBIndicator;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document("snapshotCrypto")
public class Scalper implements Serializable {
	private static final long serialVersionUID = 1L;
	public static Decimal B_MIN = Decimal.valueOf(0.10);
	public static Decimal B_MAX = Decimal.valueOf(0.90);

	@Id 
	private String id;
	private Date data;
	private String codigo;
	private double volume;			// volume negociado no timeframe
	private long negocios;			// negocios realizados no timeframe
	private double high;			// maior preco negociado no timeframe
	private double low;				// menor preco negociado no timeframe
	private double open;			
	private double close;			
	@Transient
	private boolean gravar;
	@Transient
	private boolean novo;
	@Transient
	private long totalNegocios;		// negocios realizados no dia
	@Transient
	private long totalCompras;		// compras realizados no dia
	@Transient
	private TimeSeries historico;
	@Transient
	private EMAIndicator dma;
	@Transient
	private AvgPriceIndicator avgPrice;
	@Transient
	private MinPriceIndicator lowPrice;
	@Transient
	private MaxPriceIndicator highPrice;
	@Transient
	private EMAIndicator ema;
	@Transient
	private SMAIndicator sma;
	@Transient
	private StandardDeviationIndicator std;
	@Transient
	private BollingerBandsMiddleIndicator middleBBand;
	@Transient
	private BollingerBandsLowerIndicator lowerBBand;
	@Transient
	private BollingerBandsUpperIndicator upperBBand;
	@Transient
	private PercentBIndicator pB;
	@Transient
	private PercentBIndicator pBl;
	@Transient
	private PercentBIndicator pBh;
	@Transient
	private BollingerBandWidthIndicator bbw;
	@Transient
	private PositivePercentBIndicator ppb;
	@Transient
	private NegativePercentBIndicator npb;
	@Transient
	private EMAIndicator emaPB;

	public Scalper() {}

	public Scalper(String codigo) {
		this.codigo = codigo;
		this.low = Integer.MAX_VALUE;
		this.historico = new TimeSeries(codigo+","+codigo);
		
		this.dma = new EMAIndicator(new DeltaRelativoIndicator(historico), 20);	
		this.avgPrice = new AvgPriceIndicator(historico);
		this.lowPrice = new MinPriceIndicator(historico);
		this.highPrice = new MaxPriceIndicator(historico);
		this.sma = new SMAIndicator(avgPrice, 20);
		this.std = new StandardDeviationIndicator(avgPrice, 20);
        this.middleBBand = new BollingerBandsMiddleIndicator(sma);
        this.lowerBBand = new BollingerBandsLowerIndicator(middleBBand, std);
        this.upperBBand = new BollingerBandsUpperIndicator(middleBBand, std);
        this.pB = new PercentBIndicator(avgPrice, lowerBBand, upperBBand);
        this.pBl = new PercentBIndicator(lowPrice, lowerBBand, upperBBand);
        this.pBh = new PercentBIndicator(highPrice, lowerBBand, upperBBand);
        this.bbw = new BollingerBandWidthIndicator(upperBBand, middleBBand, lowerBBand);
        this.ppb = new PositivePercentBIndicator(pBh, 200, B_MAX);
        this.npb = new NegativePercentBIndicator(pBl, 200, B_MIN);
        this.emaPB = new EMAIndicator(pB, 100);
	}
	
	public Scalper(Tick tick, String codigo) {
		this.data = tick.getEndTime().toDate();
		this.codigo = codigo;
		this.volume = tick.getVolume().toDouble();			
		this.negocios = tick.getTrades();			
		this.high = tick.getMaxPrice().toDouble();			
		this.low = tick.getMinPrice().toDouble();				
		this.open = tick.getOpenPrice().toDouble();			
		this.close = tick.getClosePrice().toDouble();			
	}

    public static Scalper fromBinanceMap(Map<String,Object> map) {
    	Scalper novo = new Scalper();
    	novo.data = new Date((Long)map.get("t"));
    	novo.codigo = (String)map.get("s");
    	novo.volume = Double.parseDouble((String)map.get("v"));	
    	novo.negocios = (Integer) map.get("n");
    	novo.high = Double.parseDouble((String)map.get("h"));	
    	novo.low = Double.parseDouble((String)map.get("l"));	
    	novo.open = Double.parseDouble((String)map.get("o"));			
    	novo.close = Double.parseDouble((String)map.get("c"));			
    	novo.gravar = (Boolean) map.get("x");
    	return novo;
    }
	
	public double calculaVwap() {
		return volume/negocios;
	}

	public void reset() {
		setOpen(0);
		setClose(0);
		setHigh(0);
		setLow(Integer.MAX_VALUE);
		negocios = 0;
		setVolume(0);
		setId(null);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Scalper other = (Scalper) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
}
