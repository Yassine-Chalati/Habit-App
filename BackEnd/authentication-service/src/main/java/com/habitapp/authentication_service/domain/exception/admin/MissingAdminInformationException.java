package com.habitapp.authentication_service.domain.exception.admin;

public class MissingAdminInformationException extends Exception{
    public MissingAdminInformationException(String message){
        super(message);
    }
}
