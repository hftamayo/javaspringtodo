package com.hftamayo.java.todo.services;

import com.hftamayo.java.todo.dto.task.TasksByStatusResponseDto;
import com.hftamayo.java.todo.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<Task> getTasks();
    Optional<Task> getTaskById(long taskId);
    List<Task> getAllTasksByStatus(boolean isActive);
    Optional<Task> getTaskByTitle(String title);
    long countAllTaskByStatus(boolean isActive);
    TasksByStatusResponseDto getTasksByStatusAndSize(boolean isActive);

    Task saveTask(Task newTask);
    Task updateTask(long taskId, Task updatedTask);
    void deleteTask(long taskId);
}

