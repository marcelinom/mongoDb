package com.spekuli.util;

import lombok.Getter;

@Getter
public enum Symbol {
    BTCUSDT("BTCUSDT", "Bitcoin"),
    ETHUSDT("ETHUSDT", "Ethereum"),
    BNBUSDT("BNBUSDT", "Binance"),
    SOLUSDT("SOLUSDT", "Solana"),
    XRPUSDT("XRPUSDT", "XRP"),
    LUNAUSDT("LUNAUSDT", "Luna"),
    ADAUSDT("ADAUSDT", "Ada Cardano"),
    AVAXUSDT("AVAXUSDT", "Avalanche"),
    DOTUSDT("DOTUSDT", "Polkadot"),
    DOGEUSDT("DOGEUSDT", "Dogecoin"),
    SHIBUSDT("SHIBUSDT", "Shiba Inu"),
    NEARUSDT("NEARUSDT", "NEAR Protocol"),
    MATICUSDT("MATICUSDT", "Polygon"),
    ATOMUSDT("ATOMUSDT", "Cosmos Hub"),
    LTCUSDT("LTCUSDT", "Litecoin"),
    TRXUSDT("TRXUSDT", "Tron"),
    ETCUSDT("ETCUSDT", "Ethereum Classic");
    
    
    private String code;
    private String nome;

    Symbol(String code, String nome) {
        this.code = code;
        this.nome = nome;
    }
    
    public static Symbol parse(String code) {
    	if (code != null) {
        	for (Symbol coin : Symbol.values()) {
        		if (code.trim().equalsIgnoreCase(coin.getCode())) 
        			return coin;
        	}
        }
    	
    	return null;
    }
    
}
