package eu.verdelhan.ta4j.indicators.trackers.bollinger;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.indicators.CachedIndicator;

public class PositivePercentBIndicator extends CachedIndicator<Decimal> {
    
    private final PercentBIndicator pbi;
    private final int timeFrame;
    private final Decimal max;
        
    public PositivePercentBIndicator(PercentBIndicator pbi, int timeFrame, Decimal max) {
        super(pbi.getTimeSeries());
        this.pbi = pbi;
        this.timeFrame = timeFrame;
        this.max = max;
    }

    @Override
    protected Decimal calculate(int index) {
        Decimal sum = Decimal.ZERO;
        for (int i = Math.max(0, index - timeFrame + 1); i <= index; i++) {
            Decimal val = pbi.getValue(i);
            if (val.isGreaterThan(max)) sum = sum.plus(Decimal.ONE);
        }
        return sum;
    }
}
