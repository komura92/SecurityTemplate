package com.example.SimpleSecurity.SimpleSecurity.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED, reason = "Bad username")
public class BadUsernameException extends RuntimeException {
    public BadUsernameException() {
        super("Bad username");
    }
}
