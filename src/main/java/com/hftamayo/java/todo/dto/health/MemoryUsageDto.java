package com.hftamayo.java.todo.dto.health;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MemoryUsageDto {
    private long total;
    private long free;

} 