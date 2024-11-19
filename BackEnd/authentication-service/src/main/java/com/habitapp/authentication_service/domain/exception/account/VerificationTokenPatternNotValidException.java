package com.menara.authentication.domain.exception.account;

public class VerificationTokenPatternNotValidException extends Exception{
    public  VerificationTokenPatternNotValidException(String message){
        super(message);
    }
}
