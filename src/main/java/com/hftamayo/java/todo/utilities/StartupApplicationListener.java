package com.hftamayo.java.todo.utilities;

import com.hftamayo.java.todo.services.DBConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

public class StartupApplicationListener {
    private final DBConnectionService dbConnectionService;

    @Autowired
    public StartupApplicationListener(DBConnectionService dbConnectionService) {
        this.dbConnectionService = dbConnectionService;
    }

    @EventListener
    public void onApplicationEvent() {
        dbConnectionService.checkDatabaseConnectionOnStartup();
    }
}
