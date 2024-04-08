package com.hftamayo.java.todo.utilities;

import java.math.BigInteger;
import java.security.SecureRandom;

public class JwtSecretGenerator {
    public static String generateSecret() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(256, random).toString(32);
    }
}
