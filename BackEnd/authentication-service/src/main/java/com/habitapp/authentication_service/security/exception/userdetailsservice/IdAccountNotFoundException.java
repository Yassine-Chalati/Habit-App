package com.menara.authentication.security.exception.userdetailsservice;

public class IdAccountNotFoundException extends RuntimeException {
    public IdAccountNotFoundException(String message){
        super(message);
    }
}
