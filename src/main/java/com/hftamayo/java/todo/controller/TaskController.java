package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.CrudOperationResponseDto;
import com.hftamayo.java.todo.entity.Task;
import com.hftamayo.java.todo.services.TaskService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.hftamayo.java.todo.dto.task.TaskResponseDto;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @GetMapping(value = "/list")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskResponseDto> getTasks() {
        return taskService.getTasks();
    }

    @GetMapping(value = "/task/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<TaskResponseDto> getTask(@PathVariable long taskId) {
        return taskService.getTask(taskId);
    }

    @GetMapping(value = "/taskbc/{criteria}/{value}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<TaskResponseDto>> getTaskByCriteria(@PathVariable String criteria, @PathVariable String value) {
        return taskService.getTaskByCriteria(criteria, value);
    }

    @GetMapping(value = "/taskbcs/{criteria}/{value}/{criteria2}/{value2}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<TaskResponseDto>> getTaskByCriterias(@PathVariable String criteria, @PathVariable String value, @PathVariable String criteria2, @PathVariable String value2) {
        return taskService.getTaskByCriterias(criteria, value, criteria2, value2);
    }

    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponseDto saveTask(@RequestBody Task task) {
        return taskService.saveTask(task);
    }

    @PutMapping(value = "/update/{taskId}")
    public TaskResponseDto updateTask(@PathVariable long taskId, @RequestBody Task task) {
        try {
            return taskService.updateTask(taskId, task);
        } catch (EntityNotFoundException enf) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/delete/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public CrudOperationResponseDto deleteTask(@PathVariable long taskId) {
        try {
            return taskService.deleteTask(taskId);
        } catch (EntityNotFoundException enf) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
