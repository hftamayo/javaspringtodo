package com.hftamayo.java.todo.utilities;

import java.security.SecureRandom;
import java.util.Base64;

public class JwtSecretGenerator {
    public static String generateSecret() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[64];
        random.nextBytes(key);
        String secret = Base64.getEncoder().encodeToString(key);
        return secret;
    }
}
