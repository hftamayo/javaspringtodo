package com.hftamayo.java.todo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class HealthCheckControllerTest {

    private HealthCheckController healthCheckController;
    private JdbcTemplate jdbcTemplate;
    private DataSource dataSource;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        dataSource = Mockito.mock(DataSource.class);
        connection = Mockito.mock(Connection.class);

        when(jdbcTemplate.getDataSource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);

        healthCheckController = new HealthCheckController(jdbcTemplate);
    }

    @Test
    public void testCheckAppHealth() {
        ResponseEntity<String> response = healthCheckController.checkAppHealth();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody()
                .contains("HealthCheck: Application is up and running."));
    }

    @Test
    public void testCheckDbHealth_Success() throws SQLException {
        ResponseEntity<String> response = healthCheckController.checkDbHealth();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody()
                .contains("HealthCheck: The connection to the data layer is up and running."));
        verify(connection, times(1)).close();
    }

    @Test
    public void testCheckDbHealth_Failure() throws SQLException {
        when(dataSource.getConnection()).thenThrow(
                new SQLException("Database connection error"));

        ResponseEntity<String> response = healthCheckController.checkDbHealth();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(true, response.getBody()
                .contains("HealthCheck: The connection to the data layer is down."));
    }
}