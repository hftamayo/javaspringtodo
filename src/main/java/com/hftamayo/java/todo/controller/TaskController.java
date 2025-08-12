package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.EndpointResponseDto;
import com.hftamayo.java.todo.dto.pagination.PageRequestDto;
import com.hftamayo.java.todo.dto.pagination.PaginatedDataDto;
import com.hftamayo.java.todo.entity.Task;
import com.hftamayo.java.todo.services.TaskService;
import com.hftamayo.java.todo.utilities.ratelimit.RateLimit;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hftamayo.java.todo.dto.task.TaskResponseDto;
import com.hftamayo.java.todo.utilities.endpoints.ResponseUtil;

@AllArgsConstructor
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @RateLimit(tokens = 15)
    @GetMapping(value = "/list")
    public ResponseEntity<EndpointResponseDto<?>> getTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "#{${pagination.default-page-size:10}}") int size,
            @RequestParam(required = false) String sort) {
        try {
            PageRequestDto pageRequestDto = new PageRequestDto(page, size, sort);
            PaginatedDataDto<TaskResponseDto> paginatedData = taskService.getPaginatedTasks(pageRequestDto);
            EndpointResponseDto<PaginatedDataDto<TaskResponseDto>> response = ResponseUtil.successResponse(paginatedData, "OPERATION_SUCCESS");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(
                ResponseUtil.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch tasks list", e),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @RateLimit(tokens = 10)
    @GetMapping(value = "/task/{taskId}")
    public ResponseEntity<EndpointResponseDto<?>> getTask(@PathVariable long taskId) {
        try {
            TaskResponseDto task = taskService.getTask(taskId);
            EndpointResponseDto<TaskResponseDto> response = ResponseUtil.successResponse(task, "OPERATION_SUCCESS");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(
                ResponseUtil.errorResponse(HttpStatus.NOT_FOUND, "Task not found", e),
                HttpStatus.NOT_FOUND
            );
        }
    }

    @RateLimit(tokens = 8)
    @GetMapping(value = "/taskbc/{criteria}/{value}")
    public ResponseEntity<EndpointResponseDto<?>> getTaskByCriteria(@PathVariable String criteria, @PathVariable String value) {
        try {
            TaskResponseDto task = taskService.getTaskByCriteria(criteria, value);
            EndpointResponseDto<TaskResponseDto> response = ResponseUtil.successResponse(task, "OPERATION_SUCCESS");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(
                ResponseUtil.errorResponse(HttpStatus.NOT_FOUND, "Task not found by criteria", e),
                HttpStatus.NOT_FOUND
            );
        }
    }

    @RateLimit(tokens = 8)
    @GetMapping(value = "/taskbcs/{criteria}/{value}/{criteria2}/{value2}")
    public ResponseEntity<EndpointResponseDto<?>> getTaskByCriterias(@PathVariable String criteria, @PathVariable String value, @PathVariable String criteria2, @PathVariable String value2) {
        try {
            TaskResponseDto task = taskService.getTaskByCriterias(criteria, value, criteria2, value2);
            EndpointResponseDto<TaskResponseDto> response = ResponseUtil.successResponse(task, "OPERATION_SUCCESS");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(
                ResponseUtil.errorResponse(HttpStatus.NOT_FOUND, "Task not found by criterias", e),
                HttpStatus.NOT_FOUND
            );
        }
    }

    @RateLimit(tokens = 5)
    @PostMapping(value = "/create")
    public ResponseEntity<EndpointResponseDto<?>> saveTask(@RequestBody Task task) {
        try {
            TaskResponseDto savedTask = taskService.saveTask(task);
            EndpointResponseDto<TaskResponseDto> response = ResponseUtil.createdResponse(savedTask, "TASK_CREATED");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(
                ResponseUtil.errorResponse(HttpStatus.BAD_REQUEST, "Failed to create task", e),
                HttpStatus.BAD_REQUEST
            );
        }
    }

    @RateLimit(tokens = 8)
    @PatchMapping(value = "/update/{taskId}")
    public ResponseEntity<EndpointResponseDto<?>> updateTask(@PathVariable long taskId, @RequestBody Task task) {
        try {
            TaskResponseDto updatedTask = taskService.updateTask(taskId, task);
            EndpointResponseDto<TaskResponseDto> response = ResponseUtil.successResponse(updatedTask, "TASK_UPDATED");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(
                ResponseUtil.errorResponse(HttpStatus.BAD_REQUEST, "Failed to update task", e),
                HttpStatus.BAD_REQUEST
            );
        }
    }

    @RateLimit(tokens = 3)
    @DeleteMapping(value = "/delete/{taskId}")
    public ResponseEntity<EndpointResponseDto<?>> deleteTask(@PathVariable long taskId) {
        try {
            taskService.deleteTask(taskId);
            EndpointResponseDto<Void> response = ResponseUtil.successResponse(null, "TASK_DELETED");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(
                ResponseUtil.errorResponse(HttpStatus.BAD_REQUEST, "Failed to delete task", e),
                HttpStatus.BAD_REQUEST
            );
        }
    }
}
