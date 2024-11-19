package com.menara.authentication.security.exception.userdetailsservice;

public class UsernameNullException extends RuntimeException {
    public UsernameNullException(String message){
        super(message);
    }
}
