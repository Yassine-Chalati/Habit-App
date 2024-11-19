package com.internship_hiring_menara.gateway_service.configuration.route;

import com.internship_hiring_menara.gateway_service.configuration.record.AccessBadge;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(2)
@AllArgsConstructor
public class ConfigurationRouteLocator {
    private AccessBadge accessBadge;


    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder){
        return routeLocatorBuilder.routes()
                .route("authentication-service",r -> r
                        .path("/account/**", "/authentication/**")
                        .filters(f -> f
                                .addRequestHeader(accessBadge.name(), accessBadge.value())
                        )
                        .uri("lb://authentication-service"))
                .route("user-service",r -> r
                        .path("/user/**")
                        .filters(f -> f
                                .addRequestHeader(accessBadge.name(), accessBadge.value())
                        )
                        .uri("lb://user-service"))
                .build();
    }
}
