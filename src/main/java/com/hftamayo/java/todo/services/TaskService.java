package com.hftamayo.java.todo.services;

import com.hftamayo.java.todo.model.Task;
import com.hftamayo.java.todo.dto.task.TaskResponseDto;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<TaskResponseDto> getTasks();
    Optional<TaskResponseDto> getTaskById(long taskId);
    Optional<List<TaskResponseDto>> getTaskByCriteria(String criteria, String value);
    Optional<List<TaskResponseDto>> getTaskByCriterias(String criteria, String value, String criteria2, String value2);

    Optional<Task> getTaskByTitle(String title);
    Task saveTask(Task newTask);
    Task updateTask(long taskId, Task updatedTask);
    void deleteTask(long taskId);
}

