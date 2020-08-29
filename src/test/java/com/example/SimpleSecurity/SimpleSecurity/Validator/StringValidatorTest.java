package com.example.SimpleSecurity.SimpleSecurity.Validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringValidatorTest {

    @Test
    void containsUpperCase() {
        assertFalse(StringValidator.containsUpperCase("asd"));
        assertTrue(StringValidator.containsUpperCase("ASD"));
        assertTrue(StringValidator.containsUpperCase("A"));
        assertTrue(StringValidator.containsUpperCase("A123"));
        assertTrue(StringValidator.containsUpperCase("As"));
        assertTrue(StringValidator.containsUpperCase("A!"));
        assertTrue(StringValidator.containsUpperCase("A!13423Qd"));
        assertFalse(StringValidator.containsUpperCase("!!!"));
        assertFalse(StringValidator.containsUpperCase("123"));
        assertFalse(StringValidator.containsUpperCase("@.,"));
    }

    @Test
    void containsLowerCase() {
        assertFalse(StringValidator.containsLowerCase("ASD"));
        assertFalse(StringValidator.containsLowerCase("A"));
        assertFalse(StringValidator.containsLowerCase("A123"));
        assertFalse(StringValidator.containsLowerCase("A!"));
        assertFalse(StringValidator.containsLowerCase("!!!"));
        assertFalse(StringValidator.containsLowerCase("123"));
        assertFalse(StringValidator.containsLowerCase("@.,"));
        assertTrue(StringValidator.containsLowerCase("A!13423Qd"));
        assertTrue(StringValidator.containsLowerCase("As"));
        assertTrue(StringValidator.containsLowerCase("asd"));
    }

    @Test
    void containsNumber() {
        assertFalse(StringValidator.containsNumber("ASD"));
        assertFalse(StringValidator.containsNumber("A"));
        assertFalse(StringValidator.containsNumber("A!"));
        assertFalse(StringValidator.containsNumber("!!!"));
        assertFalse(StringValidator.containsNumber("@.,"));
        assertFalse(StringValidator.containsNumber("As"));
        assertFalse(StringValidator.containsNumber("asd"));
        assertTrue(StringValidator.containsNumber("A!13423Qd"));
        assertTrue(StringValidator.containsNumber("123"));
        assertTrue(StringValidator.containsNumber("A123"));
    }

    @Test
    void containsDefinedSpecialCharacter() {
        assertFalse(StringValidator.containsDefinedSpecialCharacter("ASD"));
        assertFalse(StringValidator.containsDefinedSpecialCharacter("A34625DAGccxv"));
        assertFalse(StringValidator.containsDefinedSpecialCharacter("AfdbzSDG5743s"));
        assertFalse(StringValidator.containsDefinedSpecialCharacter("asd"));
        assertFalse(StringValidator.containsDefinedSpecialCharacter("123"));
        assertFalse(StringValidator.containsDefinedSpecialCharacter("A123"));
    }

    @Test
    void containsGivenCharacter() {
        assertFalse(StringValidator.containsGivenCharacter("asd123", 'x'));
        assertFalse(StringValidator.containsGivenCharacter("asd123@", 'x'));
        assertFalse(StringValidator.containsGivenCharacter("asdx123@", 'S'));
        assertTrue(StringValidator.containsGivenCharacter("asdx123@", '@'));
        assertTrue(StringValidator.containsGivenCharacter("asdx123@", '1'));
        assertTrue(StringValidator.containsGivenCharacter("Asdx123@", 'A'));
    }
}
