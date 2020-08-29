package com.example.SimpleSecurity.SimpleSecurity.Validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Slf4j
public class UserActualPasswordValidator {

    public static boolean validate(String password, String passwordHash) {
        try {
            return BCrypt.checkpw(password, passwordHash);
        } catch (Exception e) {
            log.debug("Validation failed");
            e.printStackTrace();
        }
        return false;
    }

}
