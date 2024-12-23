package com.habitapp.authentication_service.domain.exception.account;

public class VerificationTokenNotFoundException extends Exception{
    public VerificationTokenNotFoundException(String message){
        super(message);
    }
}
