package com.example.SecurityTemplate.CustomSecurityConfiguration.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Forbidden action")
public class ForbiddenActionForUser extends RuntimeException {
    public ForbiddenActionForUser(String username) {
        super("User doesn't have privileges for this action[username=" + username + "]");
    }
}
