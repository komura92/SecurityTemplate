package com.example.SecurityTemplate.CustomSecurityConfiguration.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Unauthenticated")
public class UserNotAuthenticated extends RuntimeException {
    public UserNotAuthenticated() {
        super("User not authenticated");
    }
}
