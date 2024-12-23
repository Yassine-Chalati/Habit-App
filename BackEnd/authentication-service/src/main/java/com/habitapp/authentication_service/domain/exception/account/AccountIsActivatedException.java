package com.habitapp.authentication_service.domain.exception.account;

public class AccountIsActivatedException extends Exception{
    public AccountIsActivatedException(String message){
        super(message);
    }
}
