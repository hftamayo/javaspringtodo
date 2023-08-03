package com.hftamayo.java.todo.Services;

import com.hftamayo.java.todo.Model.Task;

import java.util.List;

public interface TodoService {
    List<Task> getTasks();
    Task getTaskById(long taskId);
    Task getTaskByTitle(String title);
    List<Task> getAllTasksByStatus(boolean isActive);
    long countAllTaskByStatus(boolean isActive);

    Task saveTask(Task newTask);
    Task updateTask(long taskId, Task updatedTask);
    void deleteTask(long taskId);
}

