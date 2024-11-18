package com.habitapp.notification_service.configuration;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.habitapp.notification_service.configuration.record.RecordName;


@Configuration
@Order(1)
@EnableScheduling
@EnableConfigurationProperties({
        RecordName.class,
})
public class MainConfiguration {
}
