package com.hftamayo.java.todo.services;

import com.hftamayo.java.todo.entity.Task;
import com.hftamayo.java.todo.dto.task.TaskResponseDto;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<TaskResponseDto> getTasks();
    Optional<TaskResponseDto> getTask(long taskId);
    Optional<List<TaskResponseDto>> getTaskByCriteria(String criteria, String value);
    Optional<List<TaskResponseDto>> getTaskByCriterias(String criteria, String value, String criteria2, String value2);

    Optional<Task> getTaskByTitle(String title);
    TaskResponseDto saveTask(Task newTask);
    TaskResponseDto updateTask(long taskId, Task updatedTask);
    void deleteTask(long taskId);
    TaskResponseDto taskToDto(Task task);
}

