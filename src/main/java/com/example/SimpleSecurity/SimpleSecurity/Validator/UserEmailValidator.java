package com.example.SimpleSecurity.SimpleSecurity.Validator;

import java.util.Arrays;
import java.util.List;

public class UserEmailValidator extends StringValidator {

    // _@_._
    private static int MIN_EMAIL_CHARACTERS_NUMBER = 5;
    public static final List<Character> EMAIL_LEGAL_CHARACTERS = Arrays.asList('@', '.', '_');

    public static boolean validate(String email) {
        return minLengthCriteria(email) &&
                containsGivenCharacter(email, '@') &&
                validateSplittedByAt(email);
    }

    private static boolean minLengthCriteria(String email) {
        return email.length() >= MIN_EMAIL_CHARACTERS_NUMBER;
    }

    private static boolean dotIsNearToEnd(String email) {
        return email.charAt(email.length() - 2) == '.' ||
                email.charAt(email.length() - 3) == '.' ||
                email.charAt(email.length() - 4) == '.' ||
                email.charAt(email.length() - 5) == '.';
    }

    private static boolean validateSplittedByAt(String email) {
        String firstPart = email.substring(0, email.indexOf('@'));
        String secondPart = email.substring(email.indexOf('@') + 1);
        return firstPart.chars().noneMatch(o -> !Character.isLowerCase(o) && !Character.isDigit(o) && !EMAIL_LEGAL_CHARACTERS.contains((char) o)) &&
                !secondPart.contains("@") &&
                !secondPart.contains("..") &&
                secondPart.contains(".") &&
                secondPart.charAt(secondPart.length() - 1) != '.' &&
                secondPart.chars().noneMatch(o -> !Character.isLowerCase(o) && !Character.isDigit(o) && o != '.');
    }
}
