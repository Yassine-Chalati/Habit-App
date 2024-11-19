package com.menara.authentication.domain.exception.account;

public class AccountIsActivatedException extends Exception{
    public AccountIsActivatedException(String message){
        super(message);
    }
}
