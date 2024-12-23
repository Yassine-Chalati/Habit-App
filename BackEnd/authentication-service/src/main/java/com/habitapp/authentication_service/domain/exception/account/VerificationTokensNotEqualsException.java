package com.habitapp.authentication_service.domain.exception.account;

public class VerificationTokensNotEqualsException extends Exception{
    public VerificationTokensNotEqualsException(String message){
        super(message);
    }
}
