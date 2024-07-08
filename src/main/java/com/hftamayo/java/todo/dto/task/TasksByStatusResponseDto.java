package com.hftamayo.java.todo.dto.task;

import com.hftamayo.java.todo.model.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TasksByStatusResponseDto {
    private List<Task> tasks;
    private int count;


}