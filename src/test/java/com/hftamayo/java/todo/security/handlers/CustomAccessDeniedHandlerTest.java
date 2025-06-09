package com.hftamayo.java.todo.security.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomAccessDeniedHandlerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private PrintWriter writer;

    @InjectMocks
    private CustomAccessDeniedHandler accessDeniedHandler;

    private StringWriter stringWriter;

    @BeforeEach
    void setUp() throws IOException {
        stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
    }

    @Test
    void handle_ShouldSetForbiddenStatus() throws IOException {
        // Arrange
        AccessDeniedException exception = new AccessDeniedException("Access denied");

        // Act
        accessDeniedHandler.handle(request, response, exception);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    void handle_ShouldWriteCorrectErrorMessage() throws IOException {
        // Arrange
        AccessDeniedException exception = new AccessDeniedException("Access denied");

        // Act
        accessDeniedHandler.handle(request, response, exception);

        // Assert
        assertEquals("Access Denied: Please login first", stringWriter.toString().trim());
    }

    @Test
    void handle_ShouldHandleIOException() throws IOException {
        // Arrange
        AccessDeniedException exception = new AccessDeniedException("Access denied");
        when(response.getWriter()).thenThrow(new IOException("Failed to write response"));

        // Act & Assert
        accessDeniedHandler.handle(request, response, exception);
        // Verify that the status was still set even if writing failed
        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}