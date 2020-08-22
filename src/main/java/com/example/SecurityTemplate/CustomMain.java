package com.example.SecurityTemplate;

import java.util.Arrays;
import java.util.List;

public class CustomMain {
    public static void main(String[] args) {

        String s = "qwerty";
        String k = "qwVSD";
        String l = "rt56e";
        String d = "rt56D453e";

        System.out.println(s);
        validate(s);
        System.out.println(k);
        validate(k);
        System.out.println(l);
        validate(l);
        System.out.println(d);
        validate(d);

        //string contains any?
    }

    public static boolean validate(String newPassword) {
        if (true) {
            if (!hasUpperCase(newPassword)) {
                System.out.println("Password not contains any upper case");
                return false;
            }
        }

        if (true) {
            if (!hasLowerCase(newPassword)) {
                System.out.println("Password not contains any lower case");
                return false;
            }
        }

        if (true) {
            if (!hasNumber(newPassword)) {
                System.out.println("Password not contains any digit");
                return false;
            }
        }

        if (true) {
            if (!hasSpecialCharacter(newPassword)) {
                System.out.println("Password not contains any special character");
                return false;
            }
        }

        return true;
    }

    private static boolean hasUpperCase(String string) {
        return string.chars().anyMatch(c -> Character.isLetter(c) && Character.isUpperCase(c));
    }

    private static boolean hasLowerCase(String string) {
        return string.chars().anyMatch(c -> Character.isLetter(c) && Character.isUpperCase(c));
    }

    private static boolean hasNumber(String string) {
        return string.chars().anyMatch(Character::isDigit);
    }


    public static final List<Character> SPECIAL_CHARACTERS = Arrays.asList('$', '#', '@', '%', '&', '*', '!', '?');

    private static boolean hasSpecialCharacter(String string) {
        return string.chars().anyMatch(SPECIAL_CHARACTERS::contains); //check
    }
}
