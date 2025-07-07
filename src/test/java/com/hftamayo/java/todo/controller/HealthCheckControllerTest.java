package com.hftamayo.java.todo.controller;

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
        when(connection.isValid(5)).thenReturn(true);

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
    void checkDbHealth_WhenDatabaseIsNotConnected_ShouldReturnUnhealthyStatus() throws SQLException {
        when(jdbcTemplate.getDataSource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isValid(5)).thenReturn(false);

        ResponseEntity<String> response = healthCheckController.checkDbHealth();

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("unhealthy", response.getBody()),
                () -> verify(connection).isValid(5),
                () -> verify(dataSource).getConnection(),
                () -> verify(jdbcTemplate).getDataSource()
        );
    }

    @Test
    void checkDbHealth_WhenDatabaseConnectionFails_ShouldReturnUnhealthyStatus() throws SQLException {
        when(jdbcTemplate.getDataSource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException("Database connection error"));

        ResponseEntity<String> response = healthCheckController.checkDbHealth();

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertNotNull(response.getBody()),
                () -> assertTrue(response.getBody()
                        .contains("HealthCheck: The connection to the data layer is unhealthy")),
                () -> assertEquals("unhealthy", response.getBody()),
                () -> verify(dataSource).getConnection(),
                () -> verify(jdbcTemplate).getDataSource()
        );
    }
}