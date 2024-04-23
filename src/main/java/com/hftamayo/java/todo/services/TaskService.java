package com.hftamayo.java.todo.services;

import com.hftamayo.java.todo.model.Task;

import java.util.List;

public interface TaskService {
    List<Task> getTasks();
    List<Task> getAllTasksByStatus(boolean isActive);
    Task getTaskById(long taskId);
    Task getTaskByTitle(String title);
    long countAllTaskByStatus(boolean isActive);

    Task saveTask(Task newTask);
    Task updateTask(long taskId, Task updatedTask);
    void deleteTask(long taskId);
}

