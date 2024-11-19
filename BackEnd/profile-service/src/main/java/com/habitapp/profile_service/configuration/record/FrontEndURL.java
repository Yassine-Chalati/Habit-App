package com.habitapp.profile_service.configuration.record;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cors.front-end")
public record FrontEndURL(String url){

}
