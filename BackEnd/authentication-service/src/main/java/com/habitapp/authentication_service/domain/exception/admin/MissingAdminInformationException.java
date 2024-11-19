package com.menara.authentication.domain.exception.admin;

public class MissingAdminInformationException extends Exception{
    public MissingAdminInformationException(String message){
        super(message);
    }
}
