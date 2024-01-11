package com.in28minutes.microservices.currencyconversionservice.bean;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrencyConversion {
    private long id;
    private String from;
    private String to;
    private float conversionMultiple;
    private int quantity;
    private float totalCalculatedAmount;
}
