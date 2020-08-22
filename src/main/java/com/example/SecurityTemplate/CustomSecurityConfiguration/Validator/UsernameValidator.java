package com.example.SecurityTemplate.CustomSecurityConfiguration.Validator;

import com.example.SecurityTemplate.CustomSecurityConfiguration.Configuration.LoginConfiguration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UsernameValidator extends StringValidator {
    public static boolean validate(String username) {

        if (!isConfigured()) {
            log.error("Check your username configuration");
            return false;
        }

        if (!containsLegalCharacters(username)) {
            log.debug("Username contains illegal characters");
            return false;
        }

        if (username.chars().anyMatch(Character::isLowerCase) &&
                !LoginConfiguration.USERNAME_CAN_CONTAINS_LOWER_CASES) {
            log.debug("Username cannot contains lower cases");
            return false;
        }

        if (username.chars().anyMatch(Character::isUpperCase) &&
                !LoginConfiguration.USERNAME_CAN_CONTAINS_UPPER_CASES) {
            log.debug("Username cannot contains upper cases");
            return false;
        }

        if (username.chars().anyMatch(Character::isDigit) &&
                !LoginConfiguration.USERNAME_CAN_CONTAINS_DIGITS) {
            log.debug("Username cannot contains digits");
            return false;
        }

        if (containsSpecialCharacter(username) &&
                !LoginConfiguration.USERNAME_CAN_CONTAINS_SPECIAL_CHARACTERS) {
            log.debug("Username cannot contains special characters");
            return false;
        }

        return true;
    }

    private static boolean isConfigured() {
        return LoginConfiguration.USERNAME_CAN_CONTAINS_LOWER_CASES ||
                LoginConfiguration.USERNAME_CAN_CONTAINS_UPPER_CASES ||
                LoginConfiguration.USERNAME_CAN_CONTAINS_DIGITS ||
                LoginConfiguration.USERNAME_CAN_CONTAINS_SPECIAL_CHARACTERS;
    }
}
