package com.example.SimpleSecurity.SimpleSecurity.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "User blocked")
public class UserBlockedException extends RuntimeException {
    public UserBlockedException() {
        super("User blocked");
    }
}
