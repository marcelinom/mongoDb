package com.spekuli.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.binance.connector.client.impl.WebsocketClientImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spekuli.model.dao.MutexCustomRepository;
import com.spekuli.model.dao.ScalperRepository;
import com.spekuli.model.entity.Mutex;
import com.spekuli.model.entity.Scalper;
import com.spekuli.service.CandleStreamBinanceService;
import com.spekuli.util.Interval;
import com.spekuli.util.Symbol;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandleStreamBinanceServiceImpl implements CandleStreamBinanceService {
    private final ScalperRepository repository;
    private final MutexCustomRepository mxRepo;
    private static WebsocketClientImpl client;

	public void mineData(LocalDate end, Interval interval) {
        log.info("Mine data for: {} to: {} for {} with interval: {}", end, interval);
		if (client == null) {
			// se nenhuma instancia tiver ativado o stream, ativa. Senao, nao ativar em duplicidade
			if (mxRepo.lock() != null) {
		        client = new WebsocketClientImpl();
				ArrayList<String> streams = new ArrayList<>();
				for (Symbol coin : Symbol.values()) {
					streams.add(coin.getCode().toLowerCase()+"@kline_"+interval.getCode());			
				}
				
				client.combineStreams(streams, ((event) -> {
					Scalper tick = extractCandles(event);
					if (tick != null && tick.isGravar()) {
						repository.save(tick);
					    System.out.println("*****gravado!");
					}
				    System.out.println(event);
				}));
			}
		}
    }

	public void stopMineData() {
        log.info("Stopping Mine data");
        
		if (client != null) client.closeAllConnections();
		mxRepo.unlock();
    }

	public Mutex criaMutex(String id) {        
		Mutex mx = new Mutex();
		mx.setId(id);
		mx.setStatus(Mutex.Status.IDLE);
		return mxRepo.save(mx);
    }

    @SuppressWarnings("unchecked")
    private Scalper extractCandles(String event) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Map<String,Object> map = mapper.readValue(event, Map.class);
		    Map<String,Object> obj = (Map<String,Object>) ((Map<String,Object>)map.get("data")).get("k");
		    return Scalper.fromBinanceMap(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return null;
    }

}
