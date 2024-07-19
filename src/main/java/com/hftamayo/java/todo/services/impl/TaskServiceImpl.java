package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dao.TaskDao;
import com.hftamayo.java.todo.dto.task.TaskResponseDto;
import com.hftamayo.java.todo.dto.task.TasksByStatusResponseDto;
import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.exceptions.EntityAlreadyExistsException;
import com.hftamayo.java.todo.model.Task;
import com.hftamayo.java.todo.model.User;
import com.hftamayo.java.todo.services.TaskService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.empty;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    @Autowired
    private final TaskDao taskDao;

    public List<TaskResponseDto> getTasks() {
        List<Task> taskList = taskDao.getTasks();
        return taskList.stream().map(this::taskToDto).toList();
    }

    public Optional<Task> getTaskById(long taskId){
        return taskDao.getTaskById(taskId);
    }

    public Optional<TaskResponseDto> getTask(long taskId) {
        Optional<Task> taskOptional = getTaskById(taskId);
        if(taskOptional.isPresent()){
            Task task = taskOptional.get();
            TaskResponseDto dto = taskToDto(task);
            return Optional.of(dto);
        } else {
            return empty();
        }
    }

    public Optional<Task> getTaskByTitle(String taskTitle) {
        return taskDao.getTaskByTitle(taskTitle);
    }

    public Optional<List<TaskResponseDto>> getUserByCriteria(String criteria, String value) {
        Optional<Object> result = taskDao.getTaskByCriteria(criteria, value, false);
        return result.map(object -> {
            if (object instanceof List<?> && !((List<?>) object).isEmpty() && ((List<?>) object).get(0) instanceof Task) {
                List<Task> taskList = (List<Task>) object;
                return Optional.of(taskListToDto(taskList));
            }
            return Optional.<List<TaskResponseDto>>empty();
        }).orElse(Optional.empty());
    }

    public Optional<List<TaskResponseDto>> getUserByCriterias(String criteria, String value, String criteria2, String value2) {
        Optional<List<Task>> taskListOptional = taskDao.getTaskByCriterias(criteria, value, criteria2, value2);
        return taskListOptional.map(this::taskListToDto).map(Optional::of).orElse(Optional.empty());
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







