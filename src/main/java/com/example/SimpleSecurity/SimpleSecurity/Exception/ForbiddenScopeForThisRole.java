package com.example.SimpleSecurity.SimpleSecurity.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Bad role")
public class ForbiddenScopeForThisRole extends RuntimeException {
    public ForbiddenScopeForThisRole() {
        super("User doesn't have privileges to browse this page");
    }
}
