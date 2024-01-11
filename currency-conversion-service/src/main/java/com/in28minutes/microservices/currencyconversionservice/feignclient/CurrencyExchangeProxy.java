package com.in28minutes.microservices.currencyconversionservice.feignclient;

import com.in28minutes.microservices.currencyconversionservice.bean.CurrencyConversion;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "currency-exchange", url = "localhost:8000/currency-exchange")
@FeignClient(name = "currency-exchange")
@Component
public interface CurrencyExchangeProxy {
    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public ResponseEntity<CurrencyConversion> getCurrentExchange(@PathVariable String from,
                                                                @PathVariable String to);
}
