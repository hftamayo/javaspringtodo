package com.hftamayo.java.todo.Services;

import com.hftamayo.java.todo.Model.Task;

import java.util.List;
import java.util.Optional;

public interface TodoService {
    List<Task> getTasks();
    Task getTaskById(long taskId);
    Task getTaskByTitle(String title);
    long countAllTaskByStatus(boolean isActive);
    List<Task> getAllTasksByStatus(boolean isActive);

    Task saveTask(Task newTask);
    Task updateTask(Task updatedTask);
    void deleteTask(long taskId);
}

