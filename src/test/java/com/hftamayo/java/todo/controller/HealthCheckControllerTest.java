package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.EndpointResponseDto;
import com.hftamayo.java.todo.dto.health.AppHealthCheckDto;
import com.hftamayo.java.todo.dto.health.DbHealthCheckDto;
import com.hftamayo.java.todo.dto.health.MemoryUsageDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HealthCheckControllerTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @InjectMocks
    private HealthCheckController healthCheckController;

    @BeforeEach
    void setUp() throws SQLException {
        lenient().when(jdbcTemplate.getDataSource()).thenReturn(dataSource);
        lenient().when(dataSource.getConnection()).thenReturn(connection);
        lenient().doNothing().when(connection).close();
    }

    @Test
    void checkAppHealth_WhenApplicationIsRunning_ShouldReturnOkStatus() {
        // Act
        ResponseEntity<EndpointResponseDto<?>> response = healthCheckController.checkAppHealth();

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("OPERATION_SUCCESS", response.getBody().getResultMessage()),
                () -> assertEquals(200, response.getBody().getCode()),
                () -> assertNotNull(response.getBody().getData()),
                () -> assertTrue(response.getBody().getData() instanceof AppHealthCheckDto)
        );
    }

    @Test
    void checkDbHealth_WhenDatabaseIsConnected_ShouldReturnOkStatus() throws SQLException {
        // Act
        ResponseEntity<EndpointResponseDto<?>> response = healthCheckController.checkDbHealth();

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("OPERATION_SUCCESS", response.getBody().getResultMessage()),
                () -> assertEquals(200, response.getBody().getCode()),
                () -> assertNotNull(response.getBody().getData()),
                () -> assertTrue(response.getBody().getData() instanceof DbHealthCheckDto),
                () -> verify(connection).close(),
                () -> verify(dataSource).getConnection(),
                () -> verify(jdbcTemplate).getDataSource()
        );
    }

    @Test
    void checkDbHealth_WhenDatabaseIsNotConnected_ShouldReturnServiceUnavailable() throws SQLException {
        // Arrange
        String errorMessage = "Database connection error";
        when(dataSource.getConnection()).thenThrow(new SQLException(errorMessage));

        // Act
        ResponseEntity<EndpointResponseDto<?>> response = healthCheckController.checkDbHealth();

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("Database connection failed", response.getBody().getResultMessage()),
                () -> assertEquals(503, response.getBody().getCode()),
                () -> assertNull(response.getBody().getData()),
                () -> assertNotNull(response.getBody().getResultMessage()),
                () -> assertFalse(response.getBody().getResultMessage().isEmpty()),
                () -> verify(dataSource).getConnection(),
                () -> verify(jdbcTemplate).getDataSource()
        );
    }
}