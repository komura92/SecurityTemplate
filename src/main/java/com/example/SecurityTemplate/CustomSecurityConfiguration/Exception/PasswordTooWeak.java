package com.example.SecurityTemplate.CustomSecurityConfiguration.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED, reason = "Password too weak")
public class PasswordTooWeak extends RuntimeException {
    public PasswordTooWeak() {
        super("Password too weak");
    }
}