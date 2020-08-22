package com.example.SecurityTemplate.CustomSecurityConfiguration.Utils;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordUtils {

    public static String getNewPasswordHash(String newPassword) {
        return BCrypt.hashpw(newPassword, BCrypt.gensalt());
    }
}
