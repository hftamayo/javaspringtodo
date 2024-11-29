package com.hftamayo.java.todo.security.jwt;

import com.hftamayo.java.todo.security.managers.UserInfoProviderManager;
import io.jsonwebtoken.Claims;
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
import org.springframework.stereotype.Service;

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

    public String getToken(String username) {
        return getToken(new HashMap<>(), username);
    }

    private String getToken(Map<String, Object> extraClaims, String username) {
        extraClaims.put("sessionIdentifier", sessionIdentifier);
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(username)
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

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, String username) {
        final UserDetails userDetails = userInfoProviderManager.getUserDetails(username);
        final String tokenUsername = getUsernameFromToken(token);
        final String tokenSessionIdentifier = getClaim(token,
                claims -> claims.get("sessionIdentifier", String.class));
        return tokenUsername.equals(userDetails.getUsername()) && !isTokenExpired(token)
                && sessionIdentifier.equals(tokenSessionIdentifier);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    public long getRemainingExpirationTime(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        long diffInMillies = Math.abs(expirationDate.getTime() - new Date().getTime());
        return TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public void invalidateToken() {
        sessionIdentifier = UUID.randomUUID().toString();
    }
}