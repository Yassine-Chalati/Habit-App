package com.habitapp.profile_service.security.exception;

public class CookieFingerprintNotEqualWithJwtClaimException extends RuntimeException{
    public CookieFingerprintNotEqualWithJwtClaimException(String meassage){
        super(meassage);
    }
}
