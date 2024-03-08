package com.hftamayo.java.todo.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class TokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app-jwt-expiration-milliseconds}")
    private int jwtExpirationDate;

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();

        Date currentDate = new Date();
        Date expirateDate = new Date(currentDate.getTime() + jwtExpirationDate);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expirateDate)
                .signWith(key())
                .compact();
        return token;
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserName(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        String username = claims.getSubject();
        return username;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException mfe) {
            logger.error("Invalid JWT token: {}", mfe.getMessage());
        } catch (ExpiredJwtException eje) {
            logger.error("JWT token is expired: {}", eje.getMessage());
        } catch (UnsupportedJwtException uje) {
            logger.error("JWT token is unsupported: {}", uje.getMessage());
        } catch (IllegalArgumentException iae) {
            logger.error("JWT claims string is empty: {}", iae.getMessage());
        }
        return false;
    }

    public boolean invalidateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token);
            return false;
        } catch (MalformedJwtException mfe) {
            logger.error("Invalid JWT token: {}", mfe.getMessage());
        } catch (ExpiredJwtException eje) {
            logger.error("JWT token is expired: {}", eje.getMessage());
        } catch (UnsupportedJwtException uje) {
            logger.error("JWT token is unsupported: {}", uje.getMessage());
        } catch (IllegalArgumentException iae) {
            logger.error("JWT claims string is empty: {}", iae.getMessage());
        }
        return true;
    }


}
