package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.services.DBConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DBConnectionServiceImpl implements DBConnectionService, ApplicationRunner {

    private final DataSource dataSource;

    private boolean isDatabaseOnline = false;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        checkDatabaseConnectionOnStartup();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void checkDatabaseConnectionOnStartup() {
        isDatabaseOnline = IntStream.range(0, 3)
                .anyMatch(i -> checkDatabaseConnection());

        if (!isDatabaseOnline) {
            handleDatabaseConnectionFailure();
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

    protected void handleDatabaseConnectionFailure() {
        // Notify user and stop application
        System.out.println("Unable to establish database connection. Application will stop.");
        System.exit(1);
    }
}