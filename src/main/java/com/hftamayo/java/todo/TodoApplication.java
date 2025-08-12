package com.hftamayo.java.todo;

import com.hftamayo.java.todo.services.DBConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@RequiredArgsConstructor
public class TodoApplication {
    private final DBConnectionService dbConnectionService;

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(TodoApplication.class, args);
        TodoApplication app = context.getBean(TodoApplication.class);
        app.dbConnectionService.checkDatabaseConnectionOnStartup();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void applicationReady() {
        System.out.println("\n========================================================");
        System.out.println("        The project is up and running!");
        System.out.println("        Version: " + com.hftamayo.java.todo.utilities.version.VersionConstants.getFullVersion());
        System.out.println("        API Version: " + com.hftamayo.java.todo.utilities.version.VersionConstants.getCurrentApiVersion());
        System.out.println("========================================================\n");
    }
}
