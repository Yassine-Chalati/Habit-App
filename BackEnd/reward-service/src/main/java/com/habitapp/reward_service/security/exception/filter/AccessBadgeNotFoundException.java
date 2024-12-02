package com.habitapp.reward_service.security.exception.filter;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AccessBadgeNotFoundException extends RuntimeException{
    public AccessBadgeNotFoundException(String message){
        super(message);
    }
}
