package com.hftamayo.java.todo.services;

public interface DBConnectionService {
    void checkDatabaseConnectionOnStartup();
    int getOrder();
    void monitorDatabaseConnection();
    boolean isDatabaseOnline();
}