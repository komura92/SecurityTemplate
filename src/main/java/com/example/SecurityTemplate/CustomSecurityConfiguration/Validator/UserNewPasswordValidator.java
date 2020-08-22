package com.example.SecurityTemplate.CustomSecurityConfiguration.Validator;

import com.example.SecurityTemplate.CustomSecurityConfiguration.Configuration.LoginConfiguration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserNewPasswordValidator extends StringValidator {
    public static boolean validate(String newPassword) {

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

        if (LoginConfiguration.PASSWORD_CONTAINS_SPECIAL_CHARACTER) {
            if (!containsSpecialCharacter(newPassword)) {
                log.debug("Password not contains any special character");
                return false;
            }
        }

        return true;
    }
}
