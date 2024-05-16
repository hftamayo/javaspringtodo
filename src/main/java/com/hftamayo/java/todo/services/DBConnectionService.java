package com.hftamayo.java.todo.services;

public interface DBConnectionService {
    void checkDatabaseConnectionOnStartup();
    void monitorDatabaseConnection();
    boolean isDatabaseOnline();
}