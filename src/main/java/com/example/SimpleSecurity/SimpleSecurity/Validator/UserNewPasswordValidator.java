package com.example.SimpleSecurity.SimpleSecurity.Validator;

import com.example.SimpleSecurity.SimpleSecurity.Configuration.LoginConfiguration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserNewPasswordValidator extends StringValidator {
    public static boolean validate(String newPassword) {

        if (newPassword.length() <= LoginConfiguration.PASSWORD_MIN_LENGTH) {
            log.debug("Password is too short");
            return false;
        }

        if (!containsLegalCharacters(newPassword)) {
            log.debug("Password contains illegal characters");
            return false;
        }

        if (LoginConfiguration.PASSWORD_CONTAINS_UPPERCASE) {
            if (!containsUpperCase(newPassword)) {
                log.debug("Password not contains any upper case");
                return false;
            }
        }

        if (LoginConfiguration.PASSWORD_CONTAINS_LOWERCASE) {
            if (!containsLowerCase(newPassword)) {
                log.debug("Password not contains any lower case");
                return false;
            }
        }

        if (LoginConfiguration.PASSWORD_CONTAINS_DIGIT) {
            if (!containsNumber(newPassword)) {
                log.debug("Password not contains any digit");
                return false;
            }
        }

        if (LoginConfiguration.PASSWORD_CONTAINS_DEFINED_SPECIAL_CHARACTER) {
            if (!containsDefinedSpecialCharacter(newPassword)) {
                log.debug("Password not contains any special character");
                return false;
            }
        }

        return true;
    }
}
