package com.habitapp.authentication_service.domain.exception.account;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AccountAlreadyExistsException extends Exception {
    public AccountAlreadyExistsException(String message){
        super(message);
    }
}
