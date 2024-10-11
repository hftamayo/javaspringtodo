package com.hftamayo.java.todo.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DBConnectionServiceImplTest {

    private DBConnectionServiceImpl dbConnectionService;
    private DataSource dataSource;

    @BeforeEach
    public void setUp() {
        dataSource = Mockito.mock(DataSource.class);
        dbConnectionService = new DBConnectionServiceImpl(dataSource);
    }

    @Test
    public void testCheckDatabaseConnectionOnStartup_Success() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);

        dbConnectionService.checkDatabaseConnectionOnStartup();

        assertTrue(dbConnectionService.isDatabaseOnline());
    }

    @Test
    public void testCheckDatabaseConnectionOnStartup_Failure() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());

        dbConnectionService.checkDatabaseConnectionOnStartup();

        assertFalse(dbConnectionService.isDatabaseOnline());
    }

    @Test
    public void testMonitorDatabaseConnection_Success() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);

        dbConnectionService.monitorDatabaseConnection();

        assertTrue(dbConnectionService.isDatabaseOnline());
    }

    @Test
    public void testMonitorDatabaseConnection_Failure() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());

        dbConnectionService.monitorDatabaseConnection();

        assertFalse(dbConnectionService.isDatabaseOnline());
    }
}