package com.internship_hiring_menara.emailing_service.security.exception.filter;

public class CookieFingerprintNotEqualWithJwtClaimException extends RuntimeException{
    public CookieFingerprintNotEqualWithJwtClaimException(String message){
        super(message);
    }
}
