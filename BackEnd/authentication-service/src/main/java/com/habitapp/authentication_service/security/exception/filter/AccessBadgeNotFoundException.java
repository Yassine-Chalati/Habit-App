package com.menara.authentication.security.exception.filter;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AccessBadgeNotFoundException extends RuntimeException{
    public AccessBadgeNotFoundException(String message){
        super(message);
    }
}
