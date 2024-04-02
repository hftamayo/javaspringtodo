package com.hftamayo.java.todo;

import com.hftamayo.java.todo.Services.UserSeeder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TodoApplication {

    public static void main(String[] args) {

        SpringApplication.run(TodoApplication.class, args);
    }

}
