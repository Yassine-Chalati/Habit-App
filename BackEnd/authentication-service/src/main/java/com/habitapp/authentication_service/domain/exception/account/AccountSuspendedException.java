package com.menara.authentication.domain.exception.account;

public class AccountSuspendedException extends Exception {
    public AccountSuspendedException(String message){
        super(message);
    }
}
