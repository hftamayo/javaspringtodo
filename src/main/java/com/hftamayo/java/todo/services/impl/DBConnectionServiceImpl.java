package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.services.DBConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class DBConnectionServiceImpl implements DBConnectionService, ApplicationRunner {

    @Autowired
    private DataSource dataSource;

    private boolean isDatabaseOnline = false;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        checkDatabaseConnectionOnStartup();
    }

    @Override
    public void checkDatabaseConnectionOnStartup() {
        for (int i = 0; i < 3; i++) {
            if (checkDatabaseConnection()) {
                isDatabaseOnline = true;
                break;
            }
        }
        if (!isDatabaseOnline) {
            // Notify user and stop application
            System.out.println("Unable to establish database connection. Application will stop.");
            System.exit(1);
        }
    }

    @Scheduled(fixedRate = 60000) // Check every minute
    @Override
    public void monitorDatabaseConnection() {
        isDatabaseOnline = checkDatabaseConnection();
        // Update backend status
        System.out.println("Database status: " + (isDatabaseOnline ? "Online" : "Offline"));
    }

    @Override
    public boolean isDatabaseOnline() {
        return isDatabaseOnline;
    }

    private boolean checkDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}