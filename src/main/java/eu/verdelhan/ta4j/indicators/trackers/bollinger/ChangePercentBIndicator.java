package eu.verdelhan.ta4j.indicators.trackers.bollinger;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.indicators.CachedIndicator;

public class ChangePercentBIndicator extends CachedIndicator<Decimal> {
    
    private final PercentBIndicator pbi;
    private final int timeFrame;
    private final Decimal min;
    private final Decimal max;
        
    public ChangePercentBIndicator(PercentBIndicator pbi, int timeFrame, Decimal min, Decimal max) {
        super(pbi.getTimeSeries());
        this.pbi = pbi;
        this.timeFrame = timeFrame;
        this.min = min;
        this.max = max;
    }

    @Override
    protected Decimal calculate(int index) {
        Decimal sum = Decimal.ZERO;
        int flag = 0;
        for (int i = Math.max(0, index - timeFrame + 1); i <= index; i++) {
            Decimal val = pbi.getValue(i);
            if (val.isGreaterThan(max)) {
            	if (flag < 0) sum = sum.plus(Decimal.ONE);
        		flag = 1;
            } else if (val.isLessThan(min)) {
            	if (flag > 0) sum = sum.plus(Decimal.ONE);
            	flag = -1;
            }
        }
        return sum;
    }
}
