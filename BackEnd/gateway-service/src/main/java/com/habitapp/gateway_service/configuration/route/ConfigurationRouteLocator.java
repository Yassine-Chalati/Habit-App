package com.habitapp.gateway_service.configuration.route;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import lombok.AllArgsConstructor;

@Configuration
@Order(2)
@AllArgsConstructor
public class ConfigurationRouteLocator {

        @Bean
        public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder){
                return routeLocatorBuilder.routes()
                        .route("authentication-service",r -> r
                                .path("/authentication/**", "/account/**")
                                .filters(f -> f
                                        .addResponseHeader("Access-Control-Allow-Origin", "*")
                                )
                                .uri("lb://authentication-service")
                        )
                        .route("profile-service",r -> r
                                .path("/api/individuals/**")
                                .filters(f -> f
                                        .addResponseHeader("Access-Control-Allow-Origin", "*")
                                )
                                .uri("lb://profile-service")
                        )
                        .route("progress-service",r -> r
                                .path("/progress/**", "/reward/**", "/quote/**","/chart/**")
                                .filters(f -> f
                                        .addResponseHeader("Access-Control-Allow-Origin", "*")
                                )
                                .uri("lb://progress-service")
                        )
                        .route("habit-service",r -> r
                                .path("/habit/**")
                                .filters(f -> f
                                        .addResponseHeader("Access-Control-Allow-Origin", "*")
                                )
                                .uri("lb://habit-service")
                        )
                        .route("emailing-service",r -> r
                                .path("/email/**")
                                .filters(f -> f
                                        .addResponseHeader("Access-Control-Allow-Origin", "*")
                                )
                                .uri("lb://emailing-service")
                        )
                        .build();
        }
}
