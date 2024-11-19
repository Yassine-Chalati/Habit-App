package com.menara.authentication.domain.exception.account;

public class VerificationTokenNotFoundException extends Exception{
    public VerificationTokenNotFoundException(String message){
        super(message);
    }
}
