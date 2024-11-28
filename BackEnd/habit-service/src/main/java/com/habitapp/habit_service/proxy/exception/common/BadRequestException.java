package com.habitapp.habit_service.proxy.exception.common;

public class BadRequestException extends Exception{
    public BadRequestException(String message){
        super(message);
    }
}
