package com.habitapp.motivation_service.security.exception.filter;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AccessBadgeUnexpectedValueException extends RuntimeException {
    public AccessBadgeUnexpectedValueException(String message){
        super(message);
    }
}
