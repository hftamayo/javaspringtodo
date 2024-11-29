package com.hftamayo.java.todo.security.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

@Configuration
public class JwtConfig {

    @Bean
    public SecretKey generateKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }
}
