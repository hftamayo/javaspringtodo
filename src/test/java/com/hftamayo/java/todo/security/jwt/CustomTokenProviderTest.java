package com.hftamayo.java.todo.security.jwt;

import com.hftamayo.java.todo.security.managers.UserInfoProviderManager;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomTokenProviderTest {
    private static final String TEST_JWT_SECRET =
            "dGVzdFNlY3JldEtleUZvclRlc3RpbmdQdXJwb3Nlc09ubHkxMjM0NTY3ODkwMTIzNDU2Nzg5MA==";
    private static final int TEST_JWT_EXPIRATION = 3600000;

    @InjectMocks
    private CustomTokenProvider tokenProvider;

    @Mock
    private JwtConfig jwtConfig;

    @Mock
    private UserInfoProviderManager userInfoProviderManager;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(tokenProvider, "jwtSecret", TEST_JWT_SECRET);
        ReflectionTestUtils.setField(tokenProvider, "jwtExpirationDate", TEST_JWT_EXPIRATION);
        tokenProvider.init();
    }

    @Test
    void getToken_ShouldGenerateValidToken() {
        // Arrange
        String email = "test@example.com";

        // Act
        String token = tokenProvider.getToken(email);

        // Assert
        assertAll(
                () -> assertNotNull(token),
                () -> assertEquals(email, tokenProvider.getEmailFromToken(token)),
                () -> assertNotNull(tokenProvider.getClaim(token,
                        claims -> claims.get("sessionIdentifier", String.class)))
        );
    }

    @Test
    void getTokenType_ShouldReturnBearer() {
        assertEquals("Bearer", tokenProvider.getTokenType());
    }

    @Test
    void isTokenValid_WhenValidTokenAndUser_ShouldReturnTrue() {
        // Arrange
        String email = "test@example.com";
        String token = tokenProvider.getToken(email);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(email);
        when(userInfoProviderManager.getUserDetails(email)).thenReturn(userDetails);

        // Act & Assert
        assertTrue(tokenProvider.isTokenValid(token, email));
    }

    @Test
    void isTokenValid_WhenExpiredToken_ShouldReturnFalse() {
        // Arrange
        String email = "test@example.com";
        ReflectionTestUtils.setField(tokenProvider, "jwtExpirationDate", -3600000);
        String token = tokenProvider.getToken(email);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(email);
        when(userInfoProviderManager.getUserDetails(email)).thenReturn(userDetails);

        // Act & Assert
        assertFalse(tokenProvider.isTokenValid(token, email));
    }

    @Test
    void getRemainingExpirationTime_ShouldReturnCorrectTime() {
        // Arrange
        String email = "test@example.com";
        String token = tokenProvider.getToken(email);

        // Act
        long remainingTime = tokenProvider.getRemainingExpirationTime(token);

        // Assert
        assertTrue(remainingTime <= 1);
    }

    @Test
    void invalidateToken_ShouldChangeSessionIdentifier() {
        // Arrange
        String originalSessionId = tokenProvider.sessionIdentifier;

        // Act
        tokenProvider.invalidateToken();

        // Assert
        assertNotEquals(originalSessionId, tokenProvider.sessionIdentifier);
    }

    @Test
    void getEmailFromToken_ShouldReturnCorrectEmail() {
        // Arrange
        String email = "test@example.com";
        String token = tokenProvider.getToken(email);

        // Act
        String extractedEmail = tokenProvider.getEmailFromToken(token);

        // Assert
        assertEquals(email, extractedEmail);
    }

    @Test
    void getClaim_ShouldReturnRequestedClaim() {
        // Arrange
        String email = "test@example.com";
        String token = tokenProvider.getToken(email);

        // Act
        String subject = tokenProvider.getClaim(token, Claims::getSubject);
        Date issuedAt = tokenProvider.getClaim(token, Claims::getIssuedAt);

        // Assert
        assertAll(
                () -> assertEquals(email, subject),
                () -> assertNotNull(issuedAt),
                () -> assertTrue(issuedAt.before(new Date()))
        );
    }

    @Test
    void getToken_WithInvalidToken_ShouldThrowException() {
        // Arrange
        String invalidToken = "invalid.token.string";

        // Act & Assert
        assertThrows(Exception.class,
                () -> tokenProvider.getEmailFromToken(invalidToken));
    }
}