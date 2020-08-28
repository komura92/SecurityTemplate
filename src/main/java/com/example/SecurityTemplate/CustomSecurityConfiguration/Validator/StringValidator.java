package com.example.SecurityTemplate.CustomSecurityConfiguration.Validator;

import com.example.SecurityTemplate.CustomSecurityConfiguration.Configuration.LoginConfiguration;

public class StringValidator {

    protected static boolean containsUpperCase(String string) {
        return string.chars().anyMatch(c -> Character.isLetter(c) && Character.isUpperCase(c));
    }

    protected static boolean containsLowerCase(String string) {
        return string.chars().anyMatch(c -> Character.isLetter(c) && Character.isLowerCase(c));
    }

    protected static boolean containsNumber(String string) {
        return string.chars().anyMatch(Character::isDigit);
    }

    protected static boolean containsDefinedSpecialCharacter(String string) {
        return string.chars().anyMatch(o -> LoginConfiguration.DEFINED_SPECIAL_CHARACTERS.contains((char) o));
    }

    protected static boolean containsGivenCharacter(String string, Character c) {
        String charString = "" + c;
        return string.contains(charString);
    }

    protected static boolean containsLegalCharacters(String string) {
        return string != null &&
                !string.isEmpty() &&
                string.chars().noneMatch(o ->
                                !LoginConfiguration.DEFINED_SPECIAL_CHARACTERS.contains((char) o) &&
                                        !Character.isLetterOrDigit(o));
    }
}
