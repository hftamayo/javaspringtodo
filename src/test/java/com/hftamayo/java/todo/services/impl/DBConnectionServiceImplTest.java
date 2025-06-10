package com.hftamayo.java.todo.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DBConnectionServiceImplTest {

    private DBConnectionServiceImpl dbConnectionService;
    private DataSource dataSource;
    private Connection mockConnection;

    @BeforeEach
    public void setUp() throws SQLException {
        // Create mocks
        dataSource = mock(DataSource.class);
        mockConnection = mock(Connection.class);
        
        // Setup default behavior
        when(dataSource.getConnection()).thenReturn(mockConnection);
        
        // Create service with mocked dependencies
        dbConnectionService = new DBConnectionServiceImpl(dataSource);
    }

    @Test
    public void testCheckDatabaseConnectionOnStartup_Success() throws SQLException {
        // Arrange - connection is already mocked to succeed in setUp()

        // Act
        dbConnectionService.checkDatabaseConnectionOnStartup();

        // Assert
        assertTrue(dbConnectionService.isDatabaseOnline());
        verify(dataSource, times(1)).getConnection();
        verify(mockConnection, times(1)).close();
    }

    @Test
    public void testCheckDatabaseConnectionOnStartup_Failure() throws SQLException {
        // Arrange
        //this case must check dbConn failure with a live db
        when(dataSource.getConnection()).thenThrow(new SQLException("Database connection failed"));

        // Act
        dbConnectionService.checkDatabaseConnectionOnStartup();

        // Assert
        assertFalse(dbConnectionService.isDatabaseOnline());
        verify(dataSource, times(1)).getConnection();
    }

    @Test
    public void testMonitorDatabaseConnection_Success() throws SQLException {
        // Arrange - connection is already mocked to succeed in setUp()

        // Act
        dbConnectionService.monitorDatabaseConnection();

        // Assert
        assertTrue(dbConnectionService.isDatabaseOnline());
        verify(dataSource, times(1)).getConnection();
        verify(mockConnection, times(1)).close();
    }

    @Test
    public void testMonitorDatabaseConnection_Failure() throws SQLException {
        // Arrange
        when(dataSource.getConnection()).thenThrow(new SQLException("Database connection failed"));

        // Act
        dbConnectionService.monitorDatabaseConnection();

        // Assert
        assertFalse(dbConnectionService.isDatabaseOnline());
        verify(dataSource, times(1)).getConnection();
    }
}