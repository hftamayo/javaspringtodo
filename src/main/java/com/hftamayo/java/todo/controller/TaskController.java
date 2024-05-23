package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.model.Task;
import com.hftamayo.java.todo.services.TaskService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/todos")
public class TaskController {
    private final TaskService taskService;

    @GetMapping(value = "/alltasks")
    @ResponseStatus(HttpStatus.OK)
    public List<Task> getTasks() {
        return taskService.getTasks();
    }

    @GetMapping(value = "/gettaskbyid/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public Task getTask(@PathVariable long taskId) {
        return taskService.getTaskById(taskId);
    }

    @GetMapping(value = "/gettaskbytitle/{taskTitle}")
    @ResponseStatus(HttpStatus.OK)
    public Task getTaskByTitle(@PathVariable String taskTitle) {
        return taskService.getTaskByTitle(taskTitle);
    }

    @GetMapping(value = "/gettasksbystatus/{status}")
    @ResponseStatus(HttpStatus.OK)
    public List<Task> getTasksByStatus(@PathVariable boolean status) {
        return taskService.getAllTasksByStatus(status);
    }

    @GetMapping(value = "/counttasksbystatus/{status}")
    @ResponseStatus(HttpStatus.OK)
    public long countTasksByStatus(@PathVariable boolean status) {
        return taskService.countAllTaskByStatus(status);
    }


    @PostMapping(value = "/savetask")
    @ResponseStatus(HttpStatus.CREATED)
    public Task saveTask(@RequestBody Task task) {
        return taskService.saveTask(task);
    }

    @PutMapping(value = "/updatetask/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable long taskId, @RequestBody Task task) {
        Task updatedTask = taskService.updateTask(taskId, task);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
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
