package com.menara.authentication.domain.exception.account;

public class VerificationTokenDurationExpiredException extends Exception {
    public VerificationTokenDurationExpiredException(String message){
        super(message);
    }
}
