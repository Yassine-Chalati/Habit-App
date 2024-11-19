package com.menara.authentication.domain.exception.account;

public class VerificationTokensNotEqualsException extends Exception{
    public VerificationTokensNotEqualsException(String message){
        super(message);
    }
}
