package com.menara.authentication.domain.exception.authentication;

public class UnknownAccountTypeException extends Exception {
    public UnknownAccountTypeException(String message){
        super(message);
    }
}
