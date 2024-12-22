package com.habitapp.authentication_service.domain.exception.account;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EmailPatternNotValidException extends Exception{

    public EmailPatternNotValidException(String message){
        super(message);
    }

}
