package com.hftamayo.java.todo.security.jwt;

import com.hftamayo.java.todo.security.managers.UserInfoProviderManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationFilterTest {

    @Mock
    private UserInfoProviderManager userInfoProviderManager;

    @Mock
    private CustomTokenProvider customTokenProvider;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private AuthenticationFilter authenticationFilter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_PublicEndpoint_ShouldPassThrough() throws ServletException, IOException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/auth/login");

        // Act
        authenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(customTokenProvider);
    }

    @Test
    void doFilterInternal_NoToken_ShouldPassThrough() throws ServletException, IOException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/secured/endpoint");
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        authenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(customTokenProvider);
    }

    @Test
    void doFilterInternal_ValidToken_ShouldAuthenticateUser() throws ServletException, IOException {
        // Arrange
        String token = "valid.jwt.token";
        String email = "test@example.com";
        UserDetails userDetails = mock(UserDetails.class);

        when(request.getRequestURI()).thenReturn("/api/secured/endpoint");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(customTokenProvider.getEmailFromToken(token)).thenReturn(email);
        when(customTokenProvider.isTokenValid(token, email)).thenReturn(true);
        when(userInfoProviderManager.getUserDetails(email)).thenReturn(userDetails);

        // Act
        authenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_InvalidToken_ShouldReturnUnauthorized() throws ServletException, IOException {
        // Arrange
        String token = "invalid.jwt.token";
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(request.getRequestURI()).thenReturn("/api/secured/endpoint");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(customTokenProvider.getEmailFromToken(token)).thenThrow(new RuntimeException("Invalid token"));
        when(response.getWriter()).thenReturn(writer);

        // Act
        authenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("A problem occurred during the authentication process"));
    }

    @Test
    void doFilterInternal_InvalidTokenFormat_ShouldNotProcess() throws ServletException, IOException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/secured/endpoint");
        when(request.getHeader("Authorization")).thenReturn("InvalidFormat token");

        // Act
        authenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(customTokenProvider);
    }
}