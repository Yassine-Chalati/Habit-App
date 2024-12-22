package com.habitapp.authentication_service.domain.exception.account;

public class VerificationTokenNotRegeneratedYetException extends Exception{
    public VerificationTokenNotRegeneratedYetException(String message){
        super(message);
    }
}
