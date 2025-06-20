package com.hftamayo.java.todo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
    void setUp() {
        // No common setup needed - each test will set up its own mocks as needed
    }

    @Test
    void checkAppHealth_WhenApplicationIsRunning_ShouldReturnOkStatus() {
        ResponseEntity<String> response = healthCheckController.checkAppHealth();

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertTrue(response.getBody().contains("HealthCheck: Application is up and running")),
                () -> assertTrue(response.getBody().contains("Timestamp:"))
        );
    }

    @Test
    void checkDbHealth_WhenDatabaseIsConnected_ShouldReturnOkStatus() throws SQLException {
        when(jdbcTemplate.getDataSource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
        doNothing().when(connection).close();

        ResponseEntity<String> response = healthCheckController.checkDbHealth();

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertTrue(response.getBody().contains("HealthCheck: The connection to the data layer is up and running")),
                () -> assertTrue(response.getBody().contains("Timestamp:")),
                () -> verify(connection).close(),
                () -> verify(dataSource).getConnection(),
                () -> verify(jdbcTemplate).getDataSource()
        );
    }

    @Test
    void checkDbHealth_WhenDatabaseIsNotConnected_ShouldReturnError() throws SQLException {
        when(jdbcTemplate.getDataSource()).thenReturn(dataSource);
        String errorMessage = "Database connection error";
        when(dataSource.getConnection()).thenThrow(new SQLException(errorMessage));

        ResponseEntity<String> response = healthCheckController.checkDbHealth();

        assertAll(
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode()),
                () -> assertTrue(response.getBody().contains("HealthCheck: The connection to the data layer is down")),
                () -> assertTrue(response.getBody().contains("Timestamp:")),
                () -> assertTrue(response.getBody().contains(errorMessage)),
                () -> verify(dataSource).getConnection(),
                () -> verify(jdbcTemplate).getDataSource()
        );
    }
}