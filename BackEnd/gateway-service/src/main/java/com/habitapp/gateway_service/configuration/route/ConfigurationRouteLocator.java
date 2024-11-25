package com.habitapp.gateway_service.configuration.route;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.habitapp.gateway_service.configuration.record.AccessBadge;

import lombok.AllArgsConstructor;

@Configuration
@Order(2)
@AllArgsConstructor
public class ConfigurationRouteLocator {
        private AccessBadge accessBadge;


        @Bean
        public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder){
                return routeLocatorBuilder.routes()
                        .route("authentication-service",r -> r
                                .path("/authentication/**")
                                .filters(f -> f
                                        .addRequestHeader(accessBadge.name(), accessBadge.value())
                                )
                                .uri("lb://authentication-service"))
                        .route("profile-service",r -> r
                                .path("/profile/**")
                                .filters(f -> f
                                        .addRequestHeader(accessBadge.name(), accessBadge.value())
                                )
                                .uri("lb://profile-service"))
                        .route("progress-service",r -> r
                                .path("/progress/**")
                                .filters(f -> f
                                        .addRequestHeader(accessBadge.name(), accessBadge.value())
                                )
                                .uri("lb://progress-service"))
                        .route("habit-service",r -> r
                                .path("/habit/**")
                                .filters(f -> f
                                        .addRequestHeader(accessBadge.name(), accessBadge.value())
                                )
                                .uri("lb://habit-service"))
                        .route("notification-service",r -> r
                                .path("/notification/**")
                                .filters(f -> f
                                        .addRequestHeader(accessBadge.name(), accessBadge.value())
                                )
                                .uri("lb://notification-service"))
                        .route("emailing-service",r -> r
                                .path("/emailing/**")
                                .filters(f -> f
                                        .addRequestHeader(accessBadge.name(), accessBadge.value())
                                )
                                .uri("lb://emailing-service"))
                        .route("reward-service",r -> r
                                .path("/reward/**")
                                .filters(f -> f
                                        .addRequestHeader(accessBadge.name(), accessBadge.value())
                                )
                                .uri("lb://reward-service"))
                        .build();
        }
}
