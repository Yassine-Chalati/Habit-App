package com.habitapp.habit_service.configuration.record;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "prefix.in.application.or.bootstrap.properties.or.yaml")
public record RecordName(String name, String value) {
}