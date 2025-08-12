package com.hftamayo.java.todo.dto.health;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AppHealthCheckDto {
    private String timestamp;
    private double uptime;
    private MemoryUsageDto memoryUsage;
    private long startTime;

} 