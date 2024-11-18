package com.menara.authentication.domain.exception.account;

public class VerificationTokenNotRegeneratedYetException extends Exception{
    public VerificationTokenNotRegeneratedYetException(String message){
        super(message);
    }
}
