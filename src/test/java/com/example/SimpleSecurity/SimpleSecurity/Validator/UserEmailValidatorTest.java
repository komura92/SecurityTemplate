package com.example.SimpleSecurity.SimpleSecurity.Validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserEmailValidatorTest {

    @Test
    void validate() {
        assertTrue(UserEmailValidator.validate("as24d@o2.pl"));
        assertFalse(UserEmailValidator.validate("as2D4d@o2.p!l"));
        assertFalse(UserEmailValidator.validate("as2D4d@o2.pl"));
        assertFalse(UserEmailValidator.validate("as24d@@o2.pl"));
        assertFalse(UserEmailValidator.validate("as24do2.pl"));
        assertFalse(UserEmailValidator.validate("as!24@do2.pl"));
        assertFalse(UserEmailValidator.validate("as@4do2..pl"));
        assertFalse(UserEmailValidator.validate("a s@4do2.pl"));
        assertTrue(UserEmailValidator.validate("a_s@4do2.pl"));
        assertTrue(UserEmailValidator.validate("a.s@4do2.pl"));
        assertFalse(UserEmailValidator.validate("as@4dDo2.pl"));
    }
}
