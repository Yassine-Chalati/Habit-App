package com.menara.authentication.domain.exception.authentication;

public class UnknownAuthenticationTypeException extends Exception {
    public UnknownAuthenticationTypeException(String message){
        super(message);
    }
}
