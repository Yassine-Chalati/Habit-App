package com.internship_hiring_menara.emailing_service.security.jwt;

import com.internship_hiring_menara.emailing_service.annotation.Storage;
import lombok.Getter;
import lombok.Setter;

/**
 * AccessToken is a class for store an access jwt Generated for Authentication-Service
 * And That to authenticate and access to other microservices like User-Service Emailing-service
 */
@Getter
@Setter
@Storage
public class ServerJWTStorage {
    private String accessToken;
    private String fingerprint;
}
