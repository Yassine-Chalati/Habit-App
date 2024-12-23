package com.habitapp.authentication_service.domain.exception.account;

public class VerificationTokenDurationExpiredException extends Exception {
    public VerificationTokenDurationExpiredException(String message){
        super(message);
    }
}
