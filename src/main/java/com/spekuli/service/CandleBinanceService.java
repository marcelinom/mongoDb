package com.spekuli.service;

import java.time.LocalDate;
import java.util.List;

import com.spekuli.model.entity.Cripto;
import com.spekuli.util.Interval;
import com.spekuli.util.Symbol;

public interface CandleBinanceService {
    public void mineData(LocalDate begin, LocalDate end, Symbol symbol, Interval interval);
    public List<Cripto> extractCandles(LocalDate begin, LocalDate end, Symbol symbol, Interval interval);
    
}
