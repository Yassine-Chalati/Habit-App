package com.menara.authentication.security.exception.filter;

public class CookieTokenFingerprintNotFoundException extends RuntimeException{
    public CookieTokenFingerprintNotFoundException(String message){
        super(message);
    }
}
