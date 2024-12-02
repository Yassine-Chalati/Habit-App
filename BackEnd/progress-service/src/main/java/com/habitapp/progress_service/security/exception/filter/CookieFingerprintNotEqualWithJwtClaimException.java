package com.habitapp.progress_service.security.exception.filter;

public class CookieFingerprintNotEqualWithJwtClaimException extends RuntimeException {

    public CookieFingerprintNotEqualWithJwtClaimException(String meassage) {
        super(meassage);
    }
}
