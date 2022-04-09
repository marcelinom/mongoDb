
package eu.verdelhan.ta4j.indicators.volume;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.CachedIndicator;

public class CumulativeVolumeIndicator extends CachedIndicator<Decimal> {

    private final TimeSeries series;
    private final int timeFrame;

    public CumulativeVolumeIndicator(TimeSeries series, int timeFrame) {
        super(series);
        this.series = series;
        this.timeFrame = timeFrame;
    }

    @Override
    protected Decimal calculate(int index) {
        Decimal sum = Decimal.ZERO;
        for (int i = Math.max(0, index - timeFrame + 1); i <= index; i++) {
            sum = sum.plus(series.getTick(i).getVolume());
        }

        return sum;
    }
}
