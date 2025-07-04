package com.hftamayo.java.todo.security.jwt;

import com.hftamayo.java.todo.exceptions.AuthenticationException;
import com.hftamayo.java.todo.security.managers.UserInfoProviderManager;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class CustomTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(CustomTokenProvider.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-milliseconds}")
    private int jwtExpirationDate;

    private final JwtConfig jwtConfig;
    private final UserInfoProviderManager userInfoProviderManager;

    public volatile String sessionIdentifier = UUID.randomUUID().toString();

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getToken(String email) {
        return getToken(new HashMap<>(), email);
    }

    private String getToken(Map<String, Object> extraClaims, String email) {
        extraClaims.put("sessionIdentifier", sessionIdentifier);
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationDate))
                .signWith(getKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Key getKey() {
        return secretKey;
    }

    public String getTokenType() {
        return "Bearer";
    }

    public String getEmailFromToken(String token) {
        try {
            return getClaim(token, Claims::getSubject);
        } catch (Exception e) {
            throw new AuthenticationException("Invalid token format", e);
        }
    }

    public boolean isTokenValid(String token, String email) {
        try {
            final UserDetails userDetails = userInfoProviderManager.getUserDetails(email);
            final String tokenEmail = getEmailFromToken(token);
            final String tokenSessionIdentifier = getClaim(token,
                    claims -> claims.get("sessionIdentifier", String.class));
            return tokenEmail.equals(userDetails.getUsername()) && !isTokenExpired(token)
                    && sessionIdentifier.equals(tokenSessionIdentifier);
        } catch (ExpiredJwtException e) {
            logger.debug("Token is expired: {}", e.getMessage());
            return false;
        } catch (AuthenticationException e) {
            logger.debug("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new AuthenticationException("Failed to parse token claims", e);
        }
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = getAllClaimsFromToken(token);
            return claimsResolver.apply(claims);
        } catch (Exception e) {
            throw new AuthenticationException("Failed to extract claim from token", e);
        }
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    public long getRemainingExpirationTime(String token) {
        try {
            Date expirationDate = getExpirationDateFromToken(token);
            long diffInMillies = Math.abs(expirationDate.getTime() - new Date().getTime());
            return TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new AuthenticationException("Failed to calculate token expiration time", e);
        }
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public void invalidateToken() {
        sessionIdentifier = UUID.randomUUID().toString();
    }
}