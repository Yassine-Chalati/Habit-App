package com.menara.authentication.domain.exception.authentication;

public class GoogleIdTokenNotValidException extends Exception {
    public GoogleIdTokenNotValidException(String message){
        super(message);
    }
}