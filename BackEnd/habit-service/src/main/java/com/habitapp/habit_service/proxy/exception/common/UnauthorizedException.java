package com.habitapp.habit_service.proxy.exception.common;

public class UnauthorizedException extends Exception{
    public UnauthorizedException(String message){
        super(message);
    }
}