package com.example.SecurityTemplate.CustomSecurityConfiguration.Validator;

public class UserEmailValidator extends StringValidator {

    // _@_._
    private static int MIN_EMAIL_CHARACTERS_NUMBER = 5;

    public static boolean validate(String email) {
        //TODO validate part before and after @
        return minLengthCriteria(email) &&
                !containsUpperCase(email) &&
                containsGivenCharacter(email, '@') &&
                dotIsNearToEnd(email);
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
}
