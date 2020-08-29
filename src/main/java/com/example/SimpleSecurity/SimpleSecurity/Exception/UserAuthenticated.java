package com.example.SimpleSecurity.SimpleSecurity.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Authenticated")
public class UserAuthenticated extends RuntimeException {
    public UserAuthenticated() {
        super("User authenticated");
    }
}
