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
package eu.verdelhan.ta4j.indicators.helpers;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Indicator;
import eu.verdelhan.ta4j.indicators.CachedIndicator;

/**
 * Lowest value indicator.
 * <p>
 */
public class TradeLineIndicator extends CachedIndicator<Decimal> {

    private final Indicator<Decimal> indicator;
    private final Decimal a;
    private final Decimal b;

    public TradeLineIndicator(Indicator<Decimal> indicator, double a, double b) {
        super(indicator);
        this.indicator = indicator;
        this.a = Decimal.valueOf(a);
        this.b = Decimal.valueOf(b);
    }

    @Override
    protected Decimal calculate(int index) {
    	return a.multipliedBy(indicator.getValue(index)).plus(b);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
