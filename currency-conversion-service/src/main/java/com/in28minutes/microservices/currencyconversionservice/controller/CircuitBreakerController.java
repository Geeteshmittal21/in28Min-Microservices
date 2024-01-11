package com.in28minutes.microservices.currencyconversionservice.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RestController
@Slf4j
public class CircuitBreakerController {

    @GetMapping("/sample-api1")
    //@Retry(name = "sample-api")
    @CircuitBreaker(name="sample-api", fallbackMethod = "circuitBreakerFallbackResponse")
    public String sampleApi1(){
        log.info("Inside sampleApi1");
        ResponseEntity<String> response = null;
        try {
             response = new RestTemplate()
                    .getForEntity("http://localhost:8828/sample-api1", String.class);
        }
        catch(Exception ex){
            log.error("Inside exception : {}", ex.getLocalizedMessage());
            throw new RuntimeException("Calling Api is down");
        }
        return response.getBody();
    }

    @GetMapping("/sample-api2")
    //@Retry(name = "sample-api", fallbackMethod = "retryFallbackResponse")
    @RateLimiter(name = "default")
    public String sampleApi2(){
        log.info("Inside sampleApi2");
//        ResponseEntity<String> response = new RestTemplate()
//                .getForEntity("http://localhost:8828/sample-api2", String.class);
//        return response.getBody();
        return "Sample Api 2";
    }

    public String retryFallbackResponse(Exception ex){
        return "One of the downstream service is down. Please try after sometime. Retry!";
    }

    private String circuitBreakerFallbackResponse(Exception ex){
        return "One of the downstream service is down. Please try after sometime. Circuit Breaker!";
    }
}
