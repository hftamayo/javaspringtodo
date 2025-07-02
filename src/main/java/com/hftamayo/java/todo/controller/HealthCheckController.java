package com.hftamayo.java.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Date;

@RestController
@RequestMapping("/api/health")
public class HealthCheckController {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public HealthCheckController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/app")
    public ResponseEntity<String> checkAppHealth() {
        return new ResponseEntity<>("HealthCheck: Application is up and running. " +
                "Timestamp: " + new Date().toString(), HttpStatus.OK);
    }

    @GetMapping("/db")
    public ResponseEntity<String> checkDbHealth() {
        this.jdbcTemplate.getDataSource().getConnection().close();
        return new ResponseEntity<>("HealthCheck: The connection to the data layer is up and running. " +
                "Timestamp: " + new Date().toString(), HttpStatus.OK);
    }
}
