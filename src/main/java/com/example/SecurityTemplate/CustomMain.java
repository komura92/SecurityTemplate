package com.example.SecurityTemplate;

public class CustomMain {
    public static void main(String[] args) {
        String email = "asd123@cocolwiek.pl.";

        String firstPart = email.substring(0, email.indexOf('@'));
        String secondPart = email.substring(email.indexOf('@') + 1);

        boolean firstPartIsOk = firstPart.chars().noneMatch(o -> !Character.isLowerCase(o) && !Character.isDigit(o));

        boolean secondPartIsOk = !secondPart.contains("@") &&
                !secondPart.contains("..") &&
                secondPart.charAt(secondPart.length() -1) !='.' &&
                secondPart.chars().noneMatch(o -> !Character.isLowerCase(o) && !Character.isDigit(o) && o != '.');

        System.out.println(firstPart);
        System.out.println(secondPart);
        System.out.println(firstPartIsOk);
        System.out.println(secondPartIsOk);

    }
}
