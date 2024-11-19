package com.internship_hiring_menara.emailing_service.security.exception.filter;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AccessBadgeNotFoundException extends RuntimeException{
    public AccessBadgeNotFoundException(String message){
        super(message);
    }
}
