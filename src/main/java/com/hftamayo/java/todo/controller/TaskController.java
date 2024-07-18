package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.model.Task;
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
@RequestMapping("/api/todos")
public class TaskController {
    private final TaskService taskService;
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @GetMapping(value = "/alltasks")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskResponseDto> getTasks() {
        return taskService.getTasks();
    }

    @GetMapping(value = "/gettaskbyid/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<TaskResponseDto> getTask(@PathVariable long taskId) {
        return taskService.getTaskById(taskId);
    }

    @GetMapping(value = "/gettaskbycriteria/{criteria}/{value}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<TaskResponseDto> getTaskByCriteria(@PathVariable String criteria, @PathVariable String value) {
        return taskService.getTaskByCriteria(criteria, value);
    }

    @GetMapping(value = "/gettaskbycriterias/{criteria}/{value}/{criteria2}/{value2}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<TaskResponseDto> getTaskByCriterias(@PathVariable String criteria, @PathVariable String value, @PathVariable String criteria2, @PathVariable String value2) {
        return taskService.getTaskByCriterias(criteria, value, criteria2, value2);
    }

    @PostMapping(value = "/savetask")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponseDto saveTask(@RequestBody Task task) {
        return taskService.saveTask(task);
    }

    @PutMapping(value = "/updatetask/{taskId}")
    public TaskResponseDto updateTask(@PathVariable long taskId, @RequestBody Task task) {
        try {
            return taskService.updateTask(taskId, task);
        } catch (EntityNotFoundException enf) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/deletetask/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteTask(@PathVariable long taskId) {
        try {
            taskService.deleteTask(taskId);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException enf) {
            return new ResponseEntity<>(enf.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
