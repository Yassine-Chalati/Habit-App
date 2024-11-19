package com.habitapp.profile_service.domain.exception.person;

public class PersonNotFoundException extends Exception{
    public PersonNotFoundException(String message){
        super(message);
    }
}
