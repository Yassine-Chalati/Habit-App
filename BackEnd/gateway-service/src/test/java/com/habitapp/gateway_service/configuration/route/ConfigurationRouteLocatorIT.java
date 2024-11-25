package com.habitapp.gateway_service.configuration.route;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

import com.habitapp.gateway_service.configuration.record.AccessBadge;

@SpringBootTest
public class ConfigurationRouteLocatorIT {
    @Autowired
    private RouteLocatorBuilder routeLocatorBuilder;

    @Test
    void testCustomRouteLocator() {
        ConfigurationRouteLocator configurationRouteLocator = new ConfigurationRouteLocator(new AccessBadge("null", "null"));

        assertNotNull(configurationRouteLocator);
        assertInstanceOf(RouteLocator.class, configurationRouteLocator.customRouteLocator(routeLocatorBuilder));
    }
}
