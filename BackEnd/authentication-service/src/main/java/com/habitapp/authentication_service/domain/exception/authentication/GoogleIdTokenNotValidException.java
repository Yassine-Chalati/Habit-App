package com.habitapp.authentication_service.domain.exception.authentication;

public class GoogleIdTokenNotValidException extends Exception {
    public GoogleIdTokenNotValidException(String message){
        super(message);
    }
}
