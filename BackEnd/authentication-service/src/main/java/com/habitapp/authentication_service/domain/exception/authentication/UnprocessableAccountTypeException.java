package com.menara.authentication.domain.exception.authentication;

public class UnprocessableAccountTypeException extends Exception {
    public UnprocessableAccountTypeException(String message){
        super(message);
    }
}
