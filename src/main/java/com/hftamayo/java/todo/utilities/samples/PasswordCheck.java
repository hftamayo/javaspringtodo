package com.hftamayo.java.todo.utilities.samples;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordCheck {
    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "milucito3";
        String storedHash = "$2a$10$ZWeqtp.sCj4qIJWCf8tnnOs/f.268iKeTICNLdGLz9owOUMNzVdUu";

        boolean matches = passwordEncoder.matches(rawPassword, storedHash);
        System.out.println("Password matches: " + matches);
    }
}