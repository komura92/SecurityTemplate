package com.example.SimpleSecurity.SimpleSecurity.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User doesn't exists!")
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String login) {
        super("User with login " + login + " doesn't exists!");
    }

    public UserNotFoundException(Long id) {
        super("User with id=" + id + " doesn't exists!");
    }
}
