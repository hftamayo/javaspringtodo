package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.health.AppHealthDto;
import com.hftamayo.java.todo.dto.health.DatabaseHealthDto;
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
        ResponseEntity<AppHealthDto> response = healthCheckController.checkAppHealth();

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertNotNull(response.getBody().getTimestamp()),
                () -> assertTrue(response.getBody().getUptime() > 0),
                () -> assertNotNull(response.getBody().getMemoryUsage()),
                () -> assertTrue(response.getBody().getStartTime() > 0)
        );
    }

    @Test
    void checkDbHealth_WhenDatabaseIsConnected_ShouldReturnOkStatus() throws SQLException {
        when(jdbcTemplate.getDataSource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isValid(5)).thenReturn(true);

        ResponseEntity<DatabaseHealthDto> response = healthCheckController.checkDbHealth();

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertNotNull(response.getBody().getTimestamp()),
                () -> assertTrue(response.getBody().getConnectionTime() >= 0),
                () -> assertEquals("healthy", response.getBody().getDatabaseStatus()),
                () -> verify(connection).isValid(5),
                () -> verify(dataSource).getConnection(),
                () -> verify(jdbcTemplate).getDataSource()
        );
    }

    @Test
    void checkDbHealth_WhenDatabaseIsNotConnected_ShouldReturnUnhealthyStatus() throws SQLException {
        when(jdbcTemplate.getDataSource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isValid(5)).thenReturn(false);

        ResponseEntity<DatabaseHealthDto> response = healthCheckController.checkDbHealth();

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertNotNull(response.getBody().getTimestamp()),
                () -> assertTrue(response.getBody().getConnectionTime() >= 0),
                () -> assertEquals("unhealthy", response.getBody().getDatabaseStatus()),
                () -> verify(connection).isValid(5),
                () -> verify(dataSource).getConnection(),
                () -> verify(jdbcTemplate).getDataSource()
        );
    }

    @Test
    void checkDbHealth_WhenDatabaseConnectionFails_ShouldReturnUnhealthyStatus() throws SQLException {
        when(jdbcTemplate.getDataSource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException("Database connection error"));

        ResponseEntity<DatabaseHealthDto> response = healthCheckController.checkDbHealth();

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertNotNull(response.getBody().getTimestamp()),
                () -> assertTrue(response.getBody().getConnectionTime() >= 0),
                () -> assertEquals("unhealthy", response.getBody().getDatabaseStatus()),
                () -> verify(dataSource).getConnection(),
                () -> verify(jdbcTemplate).getDataSource()
        );
    }
}