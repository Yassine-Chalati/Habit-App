package com.habitapp.authentication_service.proxy.exception.common;

public class UnexpectedResponseBodyException extends Exception {
    public UnexpectedResponseBodyException(String message){
        super(message);
    }
}
