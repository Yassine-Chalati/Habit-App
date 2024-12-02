package com.habitapp.reward_service.security.exception.filter;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AccessBadgeUnexpectedValueException extends RuntimeException {
    public AccessBadgeUnexpectedValueException(String message){
        super(message);
    }
}
