package com.habitapp.authentication_service.domain.exception.account;

public class AccountNotFoundException extends Exception{
    public AccountNotFoundException(String message){
        super(message);
    }
}
