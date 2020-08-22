package com.example.SecurityTemplate.CustomSecurityConfiguration.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Activation link not found!")
public class InvalidActivationLink  extends RuntimeException {
    public InvalidActivationLink() {
        super("Activation link not found!");
    }
}
