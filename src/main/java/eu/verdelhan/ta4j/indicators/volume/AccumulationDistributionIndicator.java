/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 Marc de Verdelhan & respective authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package eu.verdelhan.ta4j.indicators.volume;


import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.RecursiveCachedIndicator;
import eu.verdelhan.ta4j.indicators.helpers.CloseLocationValueIndicator;

/**
 * Accumulation-distribution indicator.
 * <p>
 */
public class AccumulationDistributionIndicator extends RecursiveCachedIndicator<Decimal> {

    private TimeSeries series;
    
    private CloseLocationValueIndicator clvIndicator;

    public AccumulationDistributionIndicator(TimeSeries series) {
        super(series);
        this.series = series;
        this.clvIndicator = new CloseLocationValueIndicator(series);
    }

    @Override
    protected Decimal calculate(int index) {
        // Calculating the money flow multiplier
        Decimal moneyFlowMultiplier = clvIndicator.getValue(index);

        // Calculating the money flow volume
        Decimal moneyFlowVolume = moneyFlowMultiplier.multipliedBy(series.getTick(index).getVolume());

        return index==0? moneyFlowVolume : moneyFlowVolume.plus(getValue(index - 1));
    }
}
