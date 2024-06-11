package com.hftamayo.java.todo.security.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class JwtConfig {
    public SecretKey generateKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }
}
