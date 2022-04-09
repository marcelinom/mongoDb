package com.spekuli.service.impl;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.spekuli.config.CandleBinanceConfig;
import com.spekuli.model.dao.CriptoRepository;
import com.spekuli.model.entity.Cripto;
import com.spekuli.service.CandleBinanceService;
import com.spekuli.util.Interval;
import com.spekuli.util.Symbol;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandleBinanceServiceImpl implements CandleBinanceService {
    private final CandleBinanceConfig candleConfig;
    private final CriptoRepository repository;
    private static final long EPOCH_MILI = 24 * 60 * 60 * 1000;
    private static final int CHUNK_MAX = 1000;

    public void mineData(LocalDate begin, LocalDate end, Symbol symbol, Interval interval) {
        log.info("Mine data for: {} to: {} for {} with interval: {}", begin, end, symbol, interval);
        LocalDate currentBegin = begin.plusDays(1);
        int daysAtATime = Double.valueOf(Math.floor(CHUNK_MAX / interval.chunksPerDay())).intValue();

        while (currentBegin.isBefore(end)) {
            log.info("currentBegin: {}", currentBegin);
            LocalDate currentEnd = currentBegin.plusDays(daysAtATime);
            if (currentEnd.isAfter(end)) currentEnd = end.plusDays(1);
            List<Cripto> items = extractCandles(currentBegin, currentEnd, symbol, interval);
            repository.saveAll(items);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentBegin = currentBegin.plusDays(daysAtATime);
        }
    }



    public List<Cripto> extractCandles(LocalDate begin, LocalDate end, Symbol symbol, Interval interval) {
        log.info("Extract candles: {} to {} symbol {} interval {}", begin,end,symbol,interval);
        RestTemplate rt = new RestTemplate();
        URI url = UriComponentsBuilder.fromHttpUrl(candleConfig.getUrlPrefix() + candleConfig.getUrl())
                .queryParam(candleConfig.getUrlQueryStartTime(), begin.toEpochDay() * EPOCH_MILI)
                .queryParam(candleConfig.getUrlQuerySymbol(), symbol.getCode())
                .queryParam(candleConfig.getUrlQueryInterval(), interval.getCode())
                .queryParam(candleConfig.getUrlQueryLimit(), CHUNK_MAX)
                .queryParam(candleConfig.getUrlQueryEndTime(), end.toEpochDay() * EPOCH_MILI)
                .build().toUri();

        log.info("Url: {}", url);
        ResponseEntity<List<List<Object>>> exchange = rt.exchange(new RequestEntity<>(HttpMethod.GET, url),
                new ParameterizedTypeReference<>() {});
        List<List<Object>> response = exchange.getBody();

        if (response == null) {
            log.warn("No response from service!");
            return new ArrayList<>();
        }

        List<Cripto> collect = response.stream().map(l -> Cripto.fromArray(l, symbol)).collect(Collectors.toList());
        log.info("items extracted: {}", collect.size());
        return collect;
    }
}
