package com.in28minutes.microservices.currencyexchangeservice.controller;

import com.in28minutes.microservices.currencyexchangeservice.bean.CurrencyExchange;
import com.in28minutes.microservices.currencyexchangeservice.repository.CurrencyExchangeDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/currency-exchange")
public class CurrencyExchangeController {

    @Autowired
    private CurrencyExchangeDAO currencyExchangeDAO;
    @GetMapping("/from/{from}/to/{to}")
    public ResponseEntity<CurrencyExchange> getCurrentExchange(@PathVariable String from,
                                                              @PathVariable String to){
        log.info("Inside getCurrentExchange for converting {} to {}", from, to);
        Optional<CurrencyExchange> optionalCurrencyExchange = currencyExchangeDAO.findByFromAndTo(from, to);
        if(optionalCurrencyExchange.isEmpty()) {
            throw new RuntimeException("Exchange not found for the provided currencies");
        } else {
            CurrencyExchange exchange = optionalCurrencyExchange.get();
            return ResponseEntity.ok(exchange);
        }
    }
}
