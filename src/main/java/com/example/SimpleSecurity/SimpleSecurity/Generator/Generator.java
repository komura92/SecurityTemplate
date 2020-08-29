package com.example.SimpleSecurity.SimpleSecurity.Generator;

import net.bytebuddy.utility.RandomString;

public class Generator {
    public static String getRandomString(int length) {
        return RandomString.make(length);
    }
}
