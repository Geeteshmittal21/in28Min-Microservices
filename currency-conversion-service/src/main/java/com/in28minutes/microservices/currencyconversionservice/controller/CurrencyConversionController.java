package com.in28minutes.microservices.currencyconversionservice.controller;

import com.in28minutes.microservices.currencyconversionservice.bean.CurrencyConversion;
import com.in28minutes.microservices.currencyconversionservice.feignclient.CurrencyExchangeProxy;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.micrometer.core.lang.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanCurrentlyInCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Objects;

@RestController
@Slf4j
@RequestMapping("/currency-conversion")
public class CurrencyConversionController {

    @Autowired
    private CurrencyExchangeProxy proxy;

    @Autowired
    private RestTemplate restTemplate;
    @GetMapping("/from/{from}/to/{to}/quantity/{quantity}")
    @CircuitBreaker(name="sample-api", fallbackMethod = "fallbackResponse")
    public CurrencyConversion currencyConvertor(@PathVariable String from,
                                                @PathVariable String to,
                                                @PathVariable int quantity){
        log.info("Using rest Template");
        HashMap<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);
        ResponseEntity<CurrencyConversion> responseEntity;
        responseEntity = restTemplate.getForEntity(
                    "http://localhost:8000/currency-exchange/from/{from}/to/{to}",
                    CurrencyConversion.class,
                    uriVariables
            );

        log.info("Response is {}", responseEntity);
        CurrencyConversion currencyConversion = responseEntity.getBody();

        if(Objects.nonNull(currencyConversion)){
            currencyConversion.setTotalCalculatedAmount(quantity* currencyConversion.getConversionMultiple());
            currencyConversion.setQuantity(quantity);
        }
        return currencyConversion;
    }

    @GetMapping("/feign/from/{from}/to/{to}/quantity/{quantity}")
    @CircuitBreaker(name="sample-api", fallbackMethod = "fallbackResponse")
    public CurrencyConversion currencyConvertorFeign(@PathVariable String from,
                                                     @PathVariable String to,
                                                     @PathVariable int quantity) {
        log.info("Using feign client");
        ResponseEntity<CurrencyConversion> currencyConversion = proxy.getCurrentExchange(from, to);
        log.info("response is {}", currencyConversion);
        CurrencyConversion currConversion = currencyConversion.getBody();
        if (Objects.nonNull(currConversion)) {
            currConversion.setQuantity(quantity);
            currConversion.setTotalCalculatedAmount(quantity * currConversion.getConversionMultiple());
        }
        return currConversion;
    }
    public CurrencyConversion fallbackResponse(String from, String to, int quantity, Exception ex){
        log.error("Inside fallbackResponse with exception {}", ex.getMessage());
        return CurrencyConversion.builder().build();
    }
}
