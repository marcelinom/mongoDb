package com.spekuli.service;

import java.time.LocalDate;

import com.spekuli.util.Interval;

public interface CandleStreamBinanceService {
    public void mineData(LocalDate end, Interval interval);    
}
