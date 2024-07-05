package com.hftamayo.java.todo.dto.task;

import com.hftamayo.java.todo.model.Task;

import java.util.List;

public class TasksByStatusResponseDto {
    private List<Task> tasks;
    private int count;

    public TasksByStatusResponseDto(List<Task> tasks) {
        this.tasks = tasks;
        this.count = tasks != null ? tasks.size() : 0;
    }

    // Getters and Setters
    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        this.count = tasks != null ? tasks.size() : 0;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}