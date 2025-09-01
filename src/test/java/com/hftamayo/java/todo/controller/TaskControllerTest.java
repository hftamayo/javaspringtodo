package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.EndpointResponseDto;
import com.hftamayo.java.todo.dto.pagination.PageRequestDto;
import com.hftamayo.java.todo.dto.pagination.PaginatedDataDto;
import com.hftamayo.java.todo.dto.task.TaskResponseDto;
import com.hftamayo.java.todo.entity.Task;
import com.hftamayo.java.todo.exceptions.ResourceNotFoundException;
import com.hftamayo.java.todo.services.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @Test
    void getTasks_WhenTasksExist_ShouldReturnSuccessResponse() {
        // Arrange
        int page = 0;
        int size = 2;
        String sort = null;

        PaginatedDataDto<TaskResponseDto> paginatedData = getTasksResponseDtoPaginatedDataDto();

        when(taskService.getPaginatedTasks(any(PageRequestDto.class))).thenReturn(paginatedData);

        // Act
        ResponseEntity<EndpointResponseDto<?>> response = taskController.getTasks(page, size, sort);

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("OPERATION_SUCCESS", response.getBody().getResultMessage()),
                () -> assertEquals(200, response.getBody().getCode()),
                () -> assertNotNull(response.getBody().getData()),
                () -> verify(taskService).getPaginatedTasks(any(PageRequestDto.class))
        );
    }

    @Test
    void getTask_WhenTaskExists_ShouldReturnSuccessResponse() {
        // Arrange
        long taskId = 1L;
        TaskResponseDto taskResponse = new TaskResponseDto();
        taskResponse.setId(taskId);
        taskResponse.setTitle("Test Task");

        when(taskService.getTask(taskId)).thenReturn(taskResponse);

        // Act
        ResponseEntity<EndpointResponseDto<?>> response = taskController.getTask(taskId);

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("OPERATION_SUCCESS", response.getBody().getResultMessage()),
                () -> assertEquals(200, response.getBody().getCode()),
                () -> assertNotNull(response.getBody().getData()),
                () -> verify(taskService).getTask(taskId)
        );
    }

    @Test
    void getTask_WhenTaskNotFound_ShouldReturnNotFoundResponse() {
        // Arrange
        long taskId = 999L;
        when(taskService.getTask(taskId)).thenThrow(new ResourceNotFoundException("Task not found"));

        // Act
        ResponseEntity<EndpointResponseDto<?>> response = taskController.getTask(taskId);

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("Task not found", response.getBody().getResultMessage()),
                () -> assertEquals(404, response.getBody().getCode()),
                () -> verify(taskService).getTask(taskId)
        );
    }

    @Test
    void getTaskByCriteria_WhenTaskExists_ShouldReturnSuccessResponse() {
        // Arrange
        String criteria = "title";
        String value = "Test Task";
        TaskResponseDto taskResponse = new TaskResponseDto();
        taskResponse.setTitle(value);

        when(taskService.getTaskByCriteria(criteria, value)).thenReturn(taskResponse);

        // Act
        ResponseEntity<EndpointResponseDto<?>> response = taskController.getTaskByCriteria(criteria, value);

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("OPERATION_SUCCESS", response.getBody().getResultMessage()),
                () -> assertEquals(200, response.getBody().getCode()),
                () -> assertNotNull(response.getBody().getData()),
                () -> verify(taskService).getTaskByCriteria(criteria, value)
        );
    }

    @Test
    void getTaskByCriteria_WhenTaskNotFound_ShouldReturnNotFoundResponse() {
        // Arrange
        String criteria = "title";
        String value = "Non-existent Task";
        when(taskService.getTaskByCriteria(criteria, value)).thenThrow(new ResourceNotFoundException("Task not found"));

        // Act
        ResponseEntity<EndpointResponseDto<?>> response = taskController.getTaskByCriteria(criteria, value);

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("Task not found by criteria", response.getBody().getResultMessage()),
                () -> assertEquals(404, response.getBody().getCode()),
                () -> verify(taskService).getTaskByCriteria(criteria, value)
        );
    }

    @Test
    void getTaskByCriterias_WhenTaskExists_ShouldReturnSuccessResponse() {
        // Arrange
        String criteria = "status";
        String value = "pending";
        String criteria2 = "priority";
        String value2 = "high";
        TaskResponseDto taskResponse = new TaskResponseDto();
        taskResponse.setTitle("Test Task");

        when(taskService.getTaskByCriterias(criteria, value, criteria2, value2)).thenReturn(taskResponse);

        // Act
        ResponseEntity<EndpointResponseDto<?>> response = taskController.getTaskByCriterias(criteria, value, criteria2, value2);

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("OPERATION_SUCCESS", response.getBody().getResultMessage()),
                () -> assertEquals(200, response.getBody().getCode()),
                () -> assertNotNull(response.getBody().getData()),
                () -> verify(taskService).getTaskByCriterias(criteria, value, criteria2, value2)
        );
    }

    @Test
    void getTaskByCriterias_WhenTaskNotFound_ShouldReturnNotFoundResponse() {
        // Arrange
        String criteria = "status";
        String value = "pending";
        String criteria2 = "priority";
        String value2 = "high";
        when(taskService.getTaskByCriterias(criteria, value, criteria2, value2)).thenThrow(new ResourceNotFoundException("Task not found"));

        // Act
        ResponseEntity<EndpointResponseDto<?>> response = taskController.getTaskByCriterias(criteria, value, criteria2, value2);

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("Task not found by criterias", response.getBody().getResultMessage()),
                () -> assertEquals(404, response.getBody().getCode()),
                () -> verify(taskService).getTaskByCriterias(criteria, value, criteria2, value2)
        );
    }

    @Test
    void saveTask_WhenValidTask_ShouldReturnCreatedResponse() {
        // Arrange
        Task task = new Task();
        task.setTitle("New Task");
        task.setDescription("Task description");

        TaskResponseDto savedTask = new TaskResponseDto();
        savedTask.setTitle("New Task");
        savedTask.setDescription("Task description");

        when(taskService.saveTask(task)).thenReturn(savedTask);

        // Act
        ResponseEntity<EndpointResponseDto<?>> response = taskController.saveTask(task);

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("TASK_CREATED", response.getBody().getResultMessage()),
                () -> assertEquals(201, response.getBody().getCode()),
                () -> assertNotNull(response.getBody().getData()),
                () -> verify(taskService).saveTask(task)
        );
    }

    @Test
    void saveTask_WhenInvalidTask_ShouldReturnBadRequestResponse() {
        // Arrange
        Task task = new Task();
        // Invalid task without required fields

        when(taskService.saveTask(task)).thenThrow(new RuntimeException("Invalid task data"));

        // Act
        ResponseEntity<EndpointResponseDto<?>> response = taskController.saveTask(task);

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("Failed to create task", response.getBody().getResultMessage()),
                () -> assertEquals(400, response.getBody().getCode()),
                () -> verify(taskService).saveTask(task)
        );
    }

    @Test
    void updateTask_WhenValidTask_ShouldReturnSuccessResponse() {
        // Arrange
        long taskId = 1L;
        Task task = new Task();
        task.setTitle("Updated Task");

        TaskResponseDto updatedTask = new TaskResponseDto();
        updatedTask.setId(taskId);
        updatedTask.setTitle("Updated Task");

        when(taskService.updateTask(taskId, task)).thenReturn(updatedTask);

        // Act
        ResponseEntity<EndpointResponseDto<?>> response = taskController.updateTask(taskId, task);

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("TASK_UPDATED", response.getBody().getResultMessage()),
                () -> assertEquals(200, response.getBody().getCode()),
                () -> assertNotNull(response.getBody().getData()),
                () -> verify(taskService).updateTask(taskId, task)
        );
    }

    @Test
    void updateTask_WhenInvalidTask_ShouldReturnBadRequestResponse() {
        // Arrange
        long taskId = 1L;
        Task task = new Task();
        // Invalid task data

        when(taskService.updateTask(taskId, task)).thenThrow(new RuntimeException("Invalid task data"));

        // Act
        ResponseEntity<EndpointResponseDto<?>> response = taskController.updateTask(taskId, task);

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("Failed to update task", response.getBody().getResultMessage()),
                () -> assertEquals(400, response.getBody().getCode()),
                () -> verify(taskService).updateTask(taskId, task)
        );
    }

    @Test
    void deleteTask_WhenValidTask_ShouldReturnSuccessResponse() {
        // Arrange
        long taskId = 1L;
        doNothing().when(taskService).deleteTask(taskId);

        // Act
        ResponseEntity<EndpointResponseDto<?>> response = taskController.deleteTask(taskId);

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("TASK_DELETED", response.getBody().getResultMessage()),
                () -> assertEquals(200, response.getBody().getCode()),
                () -> verify(taskService).deleteTask(taskId)
        );
    }

    @Test
    void deleteTask_WhenTaskNotFound_ShouldReturnBadRequestResponse() {
        // Arrange
        long taskId = 999L;
        doThrow(new RuntimeException("Task not found")).when(taskService).deleteTask(taskId);

        // Act
        ResponseEntity<EndpointResponseDto<?>> response = taskController.deleteTask(taskId);

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("Failed to delete task", response.getBody().getResultMessage()),
                () -> assertEquals(400, response.getBody().getCode()),
                () -> verify(taskService).deleteTask(taskId)
        );
    }

    // Helper method
    private PaginatedDataDto<TaskResponseDto> getTasksResponseDtoPaginatedDataDto() {
        TaskResponseDto taskResponse = new TaskResponseDto();
        taskResponse.setId(1L);
        taskResponse.setTitle("Test Task");

        PaginatedDataDto<TaskResponseDto> paginatedData = new PaginatedDataDto<>();
        paginatedData.setContent(List.of(taskResponse));
        paginatedData.setTotalElements(1L);
        paginatedData.setTotalPages(1);
        paginatedData.setSize(2);
        paginatedData.setNumber(0);
        paginatedData.setFirst(true);
        paginatedData.setLast(true);
        paginatedData.setNumberOfElements(1);

        return paginatedData;
    }
}