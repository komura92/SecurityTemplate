package com.example.SimpleSecurity.SimpleSecurity.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "User no match link")
public class UserNoMatchLinkException extends RuntimeException {
    public UserNoMatchLinkException(Long user1Id, Long user2Id) {
        super("User no match link [id1=" + user1Id + "] [id2=" + user2Id + "]");
    }
}
