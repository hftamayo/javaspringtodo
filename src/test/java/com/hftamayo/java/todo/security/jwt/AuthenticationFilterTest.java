package com.hftamayo.java.todo.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hftamayo.java.todo.exceptions.AuthenticationException;
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
    private ObjectMapper objectMapper;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private AuthenticationFilter authenticationFilter;

    private StringWriter stringWriter;
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws IOException {
        SecurityContextHolder.clearContext();
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        lenient().when(response.getWriter()).thenReturn(writer);
        
        // Mock ObjectMapper to return a simple JSON string
        lenient().when(objectMapper.writeValueAsString(any())).thenReturn("{\"error\":\"test error\"}");
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

        when(request.getRequestURI()).thenReturn("/api/secured/endpoint");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(customTokenProvider.getEmailFromToken(token)).thenThrow(new AuthenticationException("Invalid token format"));

        // Act
        authenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Authentication failed: Invalid token format"));
    }

    @Test
    void doFilterInternal_ExpiredToken_ShouldReturnUnauthorized() throws ServletException, IOException {
        // Arrange
        String token = "expired.jwt.token";
        String email = "test@example.com";

        when(request.getRequestURI()).thenReturn("/api/secured/endpoint");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(customTokenProvider.getEmailFromToken(token)).thenReturn(email);
        when(customTokenProvider.isTokenValid(token, email)).thenReturn(false);

        // Act
        authenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Authentication failed: Invalid or expired token"));
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

    @Test
    void doFilterInternal_UnexpectedError_ShouldReturnInternalServerError() throws ServletException, IOException {
        // Arrange
        String token = "valid.jwt.token";

        when(request.getRequestURI()).thenReturn("/api/secured/endpoint");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(customTokenProvider.getEmailFromToken(token)).thenThrow(new RuntimeException("Unexpected error"));

        // Act
        authenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        assertTrue(stringWriter.toString().contains("A problem occurred during the authentication process"));
    }
}