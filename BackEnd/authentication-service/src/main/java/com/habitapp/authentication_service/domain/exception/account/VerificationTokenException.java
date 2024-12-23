package com.habitapp.authentication_service.domain.exception.account;

public class VerificationTokenException extends Exception{
    public VerificationTokenException(String message){
        super(message);
    }
}
