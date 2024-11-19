package com.habitapp.profile_service.security.exception;

public class CookieTokenFingerprintNotFoundException extends RuntimeException{
    public CookieTokenFingerprintNotFoundException(String message){
        super(message);
    }
}
