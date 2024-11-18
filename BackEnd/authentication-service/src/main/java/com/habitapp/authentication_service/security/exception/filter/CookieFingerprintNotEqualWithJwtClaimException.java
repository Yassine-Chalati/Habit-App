package com.menara.authentication.security.exception.filter;

public class CookieFingerprintNotEqualWithJwtClaimException extends RuntimeException{
    public CookieFingerprintNotEqualWithJwtClaimException(String message){
        super(message);
    }
}
