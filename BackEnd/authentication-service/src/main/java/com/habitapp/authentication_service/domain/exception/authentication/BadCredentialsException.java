package com.menara.authentication.domain.exception.authentication;

public class BadCredentialsException extends Exception{
    public BadCredentialsException(String message){
        super(message);
    }
}