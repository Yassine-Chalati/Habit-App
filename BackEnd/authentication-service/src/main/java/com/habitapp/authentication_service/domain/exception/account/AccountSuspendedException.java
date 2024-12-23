package com.habitapp.authentication_service.domain.exception.account;

public class AccountSuspendedException extends Exception {
    public AccountSuspendedException(String message){
        super(message);
    }
}
