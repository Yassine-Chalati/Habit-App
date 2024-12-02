package com.habitapp.progress_service.security.exception.filter;

public class CookieTokenFingerprintNotFoundException extends RuntimeException {

    public CookieTokenFingerprintNotFoundException(String message) {
        super(message);
    }
}
