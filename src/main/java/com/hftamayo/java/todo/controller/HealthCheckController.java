package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.EndpointResponseDto;
import com.hftamayo.java.todo.dto.health.AppHealthCheckDto;
import com.hftamayo.java.todo.dto.health.DbHealthCheckDto;
import com.hftamayo.java.todo.dto.health.MemoryUsageDto;
import com.hftamayo.java.todo.utilities.endpoints.ResponseUtil;
import com.hftamayo.java.todo.utilities.ratelimit.RateLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/health")
public class HealthCheckController {
    private final JdbcTemplate jdbcTemplate;
    private static final long START_TIME = Instant.now().getEpochSecond();
    private static final long START_MILLIS = System.currentTimeMillis();

    @Autowired
    public HealthCheckController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @RateLimit(tokens = 1)
    @GetMapping("/app")
    public ResponseEntity<EndpointResponseDto<?>> checkAppHealth() {
        try {
            String timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
            double uptime = (System.currentTimeMillis() - START_MILLIS) / 1000.0;
            long startTime = START_TIME;

            Runtime runtime = Runtime.getRuntime();
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            MemoryUsageDto memoryUsage = new MemoryUsageDto(totalMemory, freeMemory);

            AppHealthCheckDto dto = new AppHealthCheckDto(timestamp, uptime, memoryUsage, startTime);
            EndpointResponseDto<AppHealthCheckDto> response = ResponseUtil
                    .successResponse(dto, "OPERATION_SUCCESS");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            EndpointResponseDto<String> errorResponse = new EndpointResponseDto<>(
                500,
                "Application health check failed",
                List.of(e.getMessage())
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RateLimit(tokens = 1)
    @GetMapping("/db")
    public ResponseEntity<EndpointResponseDto<?>> checkDbHealth() {
        String timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
        double connectionTime = 0.0;
        String databaseStatus = "unhealthy";
        long start = System.nanoTime();
        try {
            var connection = this.jdbcTemplate.getDataSource().getConnection();
            connection.close();
            connectionTime = (System.nanoTime() - start) / 1_000_000_000.0;
            databaseStatus = "healthy";
            DbHealthCheckDto dto = new DbHealthCheckDto(timestamp, connectionTime, databaseStatus);
            EndpointResponseDto<DbHealthCheckDto> response = ResponseUtil
                    .successResponse(dto, "OPERATION_SUCCESS");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (SQLException e) {
            connectionTime = (System.nanoTime() - start) / 1_000_000_000.0;
            EndpointResponseDto<String> errorResponse = new EndpointResponseDto<>(
                503,
                "Database connection failed",
                List.of(e.getMessage())
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
