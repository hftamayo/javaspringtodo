package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dao.TaskDao;
import com.hftamayo.java.todo.dto.task.TaskResponseDto;
import com.hftamayo.java.todo.dto.task.TasksByStatusResponseDto;
import com.hftamayo.java.todo.exceptions.EntityAlreadyExistsException;
import com.hftamayo.java.todo.model.Task;
import com.hftamayo.java.todo.services.TaskService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    @Autowired
    private final TaskDao taskDao;

    public List<TaskResponseDto> getTasks() {
        return taskDao.getTasks();
    }

    public Optional<Task> getTaskById(long taskId) {
        return taskDao.getTaskById(taskId);
    }

    public List<Task> getAllTasksByStatus(boolean taskStatus) {
        return taskDao.getTasksByStatus(taskStatus);
    }

    public Optional<Task> getTaskByTitle(String taskTitle) {
        return taskDao.getTaskByTitle(taskTitle);
    }

    public long countAllTaskByStatus(boolean taskStatus) {
        List<Task> tasks = taskDao.getTasksByStatus(taskStatus);
        return tasks.size();
    }

    public TasksByStatusResponseDto getTasksByStatusAndSize(boolean taskStatus) {
        List<Task> tasks = taskDao.getTasksByStatus(taskStatus);
        int count = tasks.size();
        return new TasksByStatusResponseDto(tasks, count);
    }

    @Transactional
    @Override
    public Task saveTask(Task task) {
        Optional<Task> requestedTask = getTaskByTitle(task.getTitle());
        if (requestedTask.isPresent()) {
            throw new EntityAlreadyExistsException("Title already exists");
        }
        return taskDao.saveTask(task);
    }

    @Transactional
    @Override
    public Task updateTask(long taskId, Task task) {
        Optional<Task> requestedTask = getTaskById(taskId);
        if (requestedTask.isPresent()) {
            Task updateTask = requestedTask.get();
            updateTask.setTitle(task.getTitle());
            updateTask.setDescription(task.getDescription());
            updateTask.setStatus(task.isStatus());
            return taskDao.updateTask(taskId, updateTask);
        } else {
            throw new EntityNotFoundException("Task does not exist");
        }
    }

    @Transactional
    @Override
    public void deleteTask(long taskId) {
        Optional<Task> requestedTask = getTaskById(taskId);
        if (requestedTask.isPresent()) {
            taskDao.deleteTask(requestedTask.get().getId());
        } else {
            throw new EntityNotFoundException("Task does not exist");
        }
    }

    @Override
    public TaskResponseDto taskToDto(Task task) {
        String owner = task.getUser().getUsername();

        return new TaskResponseDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isStatus(),
                task.getDateAdded().toString(),
                owner
        );
    }

    private List<TaskResponseDto> taskListToDto(List<Task> tasks) {
        return tasks.stream().map(this::taskToDto).collect(Collectors.toList());
    }

}







