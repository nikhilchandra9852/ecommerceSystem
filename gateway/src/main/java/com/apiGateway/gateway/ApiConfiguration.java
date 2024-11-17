package com.apiGateway.gateway;


import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ApiConfiguration {

    @Bean
    public RouteLocator getRouteLocator(RouteLocatorBuilder builder){
        return  builder.routes()
                        .route(p->p.path("/registration_service/**").uri("lb://REGISTRATIONSERVICE"))



                .build();
    }

}
