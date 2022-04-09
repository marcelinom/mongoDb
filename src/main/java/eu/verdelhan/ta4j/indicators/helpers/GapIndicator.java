package eu.verdelhan.ta4j.indicators.helpers;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Indicator;
import eu.verdelhan.ta4j.indicators.CachedIndicator;

/**
 * <p>
 */
public class GapIndicator extends CachedIndicator<Decimal> {

    private final Indicator<Decimal> indicator;

    private final int timeFrame;
    private Decimal margem;

    public GapIndicator(Indicator<Decimal> indicator, int timeFrame, Decimal margem) {
        super(indicator);
        this.indicator = indicator;
        this.timeFrame = timeFrame;
        this.margem = margem;
    }

    @Override
    protected Decimal calculate(int index) {
    	if (index>0 && timeFrame>0 && index-timeFrame>=0 && index<indicator.getTimeSeries().getTickCount()) {
            Decimal variacao = indicator.getValue(index).minus(indicator.getValue(index-timeFrame)).dividedBy(indicator.getValue(index-timeFrame));
            if (variacao.abs().isGreaterThanOrEqual(margem)) {
            	return variacao;
            }
        } 
    	
    	return Decimal.ZERO;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " timeFrame: " + timeFrame;
    }
}
