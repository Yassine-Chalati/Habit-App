package com.habitapp.profile_service.security.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AccessBadgeNotFoundException extends RuntimeException{
    public AccessBadgeNotFoundException(String message){
        super(message);
    }
}
