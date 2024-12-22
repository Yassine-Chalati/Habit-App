package com.habitapp.authentication_service.domain.exception.account;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PasswordPatternNotValidException extends Exception{
    public PasswordPatternNotValidException(String message){
        super(message);
    }
}
