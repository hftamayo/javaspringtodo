package com.hftamayo.java.todo;

import com.hftamayo.java.todo.services.DBConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

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
}
