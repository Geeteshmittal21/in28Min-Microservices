package com.in28minutes.microservices.apigateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder){
        return builder.routes()
                .route( p -> p.path("/get")
                        .uri("http://httpbin.org:80"))
                .route( p -> p.path("/currency-exchange/**")
                        .uri("lb://currency-exchange"))
                .route(p -> p.path("/currency-conversion/**")
                        .uri("lb://currency-conversion"))
                .route(p->p.path("/cur-conv/**")
                        .filters(f -> f.rewritePath("/cur-conv/",
                                "/currency-conversion/"
                                ))
                        .uri("lb://currency-conversion"))
                .build();
    }
}
