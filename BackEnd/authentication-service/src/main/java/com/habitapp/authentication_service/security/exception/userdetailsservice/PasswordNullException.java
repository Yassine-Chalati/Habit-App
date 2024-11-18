package com.menara.authentication.security.exception.userdetailsservice;

public class PasswordNullException extends RuntimeException{

    public PasswordNullException(String message){
        super(message);
    }
}
