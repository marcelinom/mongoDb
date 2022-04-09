package eu.verdelhan.ta4j.indicators.trackers.bollinger;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.indicators.CachedIndicator;

public class NegativePercentBIndicator extends CachedIndicator<Decimal> {
    
    private final PercentBIndicator pbi;
    private final int timeFrame;
    private final Decimal min;
        
    public NegativePercentBIndicator(PercentBIndicator pbi, int timeFrame, Decimal min) {
        super(pbi.getTimeSeries());
        this.pbi = pbi;
        this.timeFrame = timeFrame;
        this.min = min;
    }

    @Override
    protected Decimal calculate(int index) {
        Decimal sum = Decimal.ZERO;
        for (int i = Math.max(0, index - timeFrame + 1); i <= index; i++) {
            Decimal val = pbi.getValue(i);
            if (val.isLessThan(min)) sum = sum.plus(Decimal.ONE);
        }
        return sum;
    }
}
