package com.spekuli.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("candle.binance")
@Data
public class CandleBinanceConfig {
    private String urlPrefix;
    private String url;
    private String urlQuerySymbol;
    private String urlQueryInterval;
    private String urlQueryStartTime;
    private String urlQueryEndTime;
    private String urlQueryLimit;
}
