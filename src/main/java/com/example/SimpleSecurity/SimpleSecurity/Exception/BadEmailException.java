package com.example.SimpleSecurity.SimpleSecurity.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED, reason = "Bad email")
public class BadEmailException extends RuntimeException {
    public BadEmailException() {
        super("Bad email");
    }
}
