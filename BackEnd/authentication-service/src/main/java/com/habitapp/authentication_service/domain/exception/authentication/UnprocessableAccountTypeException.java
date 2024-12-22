package com.habitapp.authentication_service.domain.exception.authentication;

public class UnprocessableAccountTypeException extends Exception {
    public UnprocessableAccountTypeException(String message){
        super(message);
    }
}
