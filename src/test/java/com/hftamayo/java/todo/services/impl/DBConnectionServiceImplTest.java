package com.hftamayo.java.todo.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DBConnectionServiceImplTest {

    private TestableDBConnectionServiceImpl dbConnectionService;
    private DataSource dataSource;
    private Connection mockConnection;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    // Test-specific implementation that doesn't actually exit
    private static class TestableDBConnectionServiceImpl extends DBConnectionServiceImpl {
        private boolean shouldExit = false;

        public TestableDBConnectionServiceImpl(DataSource dataSource) {
            super(dataSource);
        }

        @Override
        protected void handleDatabaseConnectionFailure() {
            System.out.println("Unable to establish database connection. Application will stop.");
            shouldExit = true;
        }

        public boolean shouldExit() {
            return shouldExit;
        }
    }

    @BeforeEach
    public void setUp() throws SQLException {
        // Create mocks
        dataSource = mock(DataSource.class);
        mockConnection = mock(Connection.class);
        
        // Setup default behavior
        when(dataSource.getConnection()).thenReturn(mockConnection);
        
        // Create service with mocked dependencies
        dbConnectionService = new TestableDBConnectionServiceImpl(dataSource);

        // Setup output capture
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
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
        when(dataSource.getConnection()).thenThrow(new SQLException("Database connection failed"));

        // Act
        dbConnectionService.checkDatabaseConnectionOnStartup();

        // Assert
        assertFalse(dbConnectionService.isDatabaseOnline());
        assertTrue(dbConnectionService.shouldExit());
        assertTrue(outContent.toString().contains("Unable to establish database connection. Application will stop."));
        verify(dataSource, times(3)).getConnection();
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