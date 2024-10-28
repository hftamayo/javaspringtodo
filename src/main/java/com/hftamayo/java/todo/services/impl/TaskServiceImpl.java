package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dao.TaskDao;
import com.hftamayo.java.todo.dto.task.TaskResponseDto;
import com.hftamayo.java.todo.exceptions.EntityAlreadyExistsException;
import com.hftamayo.java.todo.model.Task;
import com.hftamayo.java.todo.services.TaskService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.empty;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

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

    public Optional<List<TaskResponseDto>> getTaskByCriteria(String criteria, String value) {
        Optional<Object> result = taskDao.getTaskByCriteria(criteria, value, false);
        return result.map(object -> {
            if (object instanceof List<?> && !((List<?>) object).isEmpty() && ((List<?>) object).get(0) instanceof Task) {
                List<Task> taskList = (List<Task>) object;
                return Optional.of(taskListToDto(taskList));
            }
            return Optional.<List<TaskResponseDto>>empty();
        }).orElse(Optional.empty());
    }

    public Optional<List<TaskResponseDto>> getTaskByCriterias(String criteria, String value, String criteria2, String value2) {
        Optional<List<Task>> taskListOptional = taskDao.getTaskByCriterias(criteria, value, criteria2, value2);
        return taskListOptional.map(this::taskListToDto).map(Optional::of).orElse(Optional.empty());
    }

    @Transactional
    @Override
    public TaskResponseDto saveTask(Task newTask) {
        Optional<Task> requestedTask = getTaskByTitle(newTask.getTitle());
        if (!requestedTask.isPresent()) {
            Task savedTask = taskDao.saveTask(newTask);
            TaskResponseDto dto = taskToDto(savedTask);
            return dto;
        } else {
            throw new EntityAlreadyExistsException("Task title already exists: " +
                    requestedTask.get().getTitle());
        }
    }

@Transactional
    @Override
    public TaskResponseDto updateTask(long taskId, Task updatedTask) {
        Optional<Task> requestedTaskOptional = getTaskById(taskId);
        if (requestedTaskOptional.isPresent()) {
            Map<String, Object> propertiesToUpdate = new HashMap<>();
            propertiesToUpdate.put("title", updatedTask.getTitle());
            propertiesToUpdate.put("description", updatedTask.getDescription());
            propertiesToUpdate.put("status", updatedTask.isStatus());
            Task task = taskDao.updateTask(taskId, propertiesToUpdate);
            return taskToDto(task);
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







