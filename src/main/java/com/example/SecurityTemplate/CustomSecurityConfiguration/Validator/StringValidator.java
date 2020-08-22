package com.example.SecurityTemplate.CustomSecurityConfiguration.Validator;

import com.example.SecurityTemplate.CustomSecurityConfiguration.Configuration.LoginConfiguration;

public class StringValidator {

    protected static boolean containsUpperCase(String string) {
        return string.chars().anyMatch(c -> Character.isLetter(c) && Character.isUpperCase(c));
    }

    protected static boolean containsLowerCase(String string) {
        return string.chars().anyMatch(c -> Character.isLetter(c) && Character.isUpperCase(c));
    }

    protected static boolean containsNumber(String string) {
        return string.chars().anyMatch(Character::isDigit);
    }

    protected static boolean containsSpecialCharacter(String string) {
        return string.chars().anyMatch(LoginConfiguration.SPECIAL_CHARACTERS::contains); //check
    }

    protected static boolean containsGivenCharacter(String string, Character c) {
        String charString = "" + c;
        return string.contains(charString);
    }

    protected static boolean containsLegalCharacters(String string) {
        return string.chars().noneMatch(o -> !LoginConfiguration.SPECIAL_CHARACTERS.contains(o) || !Character.isLetterOrDigit(o));
    }
}
