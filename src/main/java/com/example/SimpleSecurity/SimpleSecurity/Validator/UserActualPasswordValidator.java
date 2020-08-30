package com.example.SimpleSecurity.SimpleSecurity.Validator;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class UserActualPasswordValidator {

    public static boolean validate(String password, String passwordHash) {
        try {
            return BCrypt.checkpw(password, passwordHash);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
