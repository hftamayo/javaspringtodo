package com.hftamayo.java.todo.dto.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class TaskResponseDto {
    private Long id;
    private String title;
    private String description;
    private boolean status;
    private String dateAdded;
    private String owner;
}
