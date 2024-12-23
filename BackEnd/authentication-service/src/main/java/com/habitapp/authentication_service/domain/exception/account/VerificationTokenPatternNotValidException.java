package com.habitapp.authentication_service.domain.exception.account;

public class VerificationTokenPatternNotValidException extends Exception{
    public  VerificationTokenPatternNotValidException(String message){
        super(message);
    }
}
