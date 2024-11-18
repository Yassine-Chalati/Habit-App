package com.menara.authentication.domain.exception.account;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AccountAlreadyExistsException extends Exception {
    public AccountAlreadyExistsException(String message){
        super(message);
    }
}
