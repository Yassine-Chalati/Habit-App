package com.menara.authentication.proxy.exception.common;

public class BadRequestException extends Exception{
    public BadRequestException(String message){
        super(message);
    }
}