package com.habitapp.profile_service.security.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AccessBadgeUnexpectedValueException extends RuntimeException {
    public AccessBadgeUnexpectedValueException(String message){
        super(message);
    }
}
