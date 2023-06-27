package com.hftamayo.java.todo.Services;

import com.hftamayo.java.todo.Model.Task;

import java.util.List;
import java.util.Optional;

public interface TodoService {
    List<Task> getTasks();
    Optional<Task> getTaskById(long taskId);
    Task saveTask(Task newTask);
    Task updateTask(Task updatedTask);
    void deleteTask(long taskId);
}

