package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.CrudOperationResponseDto;
import com.hftamayo.java.todo.dto.task.TaskResponseDto;
import com.hftamayo.java.todo.entity.Task;
import com.hftamayo.java.todo.services.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

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
        CrudOperationResponseDto<TaskResponseDto> expectedResponse = new CrudOperationResponseDto<>();
        expectedResponse.setCode(200);
        expectedResponse.setResultMessage("OPERATION SUCCESSFUL");
        expectedResponse.setData((TaskResponseDto) List.of(new TaskResponseDto()));

        when(taskService.getTasks()).thenReturn(expectedResponse);

        // Act
        CrudOperationResponseDto<TaskResponseDto> response = taskController.getTasks();

        // Assert
        assertAll(
                () -> assertEquals(200, response.getCode()),
                () -> assertEquals("OPERATION SUCCESSFUL", response.getResultMessage()),
                () -> assertNotNull(response.getData()),
                () -> verify(taskService).getTasks()
        );
    }

    @Test
    void getTask_WhenTaskExists_ShouldReturnSuccessResponse() {
        // Arrange
        long taskId = 1L;
        CrudOperationResponseDto<TaskResponseDto> expectedResponse = new CrudOperationResponseDto<>();
        expectedResponse.setCode(200);
        expectedResponse.setResultMessage("OPERATION SUCCESSFUL");
        expectedResponse.setData(new TaskResponseDto());

        when(taskService.getTask(taskId)).thenReturn(expectedResponse);

        // Act
        CrudOperationResponseDto<TaskResponseDto> response = taskController.getTask(taskId);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getCode()),
                () -> assertEquals("OPERATION SUCCESSFUL", response.getResultMessage()),
                () -> assertNotNull(response.getData()),
                () -> verify(taskService).getTask(taskId)
        );
    }

    @Test
    void getTaskByCriteria_WhenTaskExists_ShouldReturnSuccessResponse() {
        // Arrange
        String criteria = "status";
        String value = "pending";
        CrudOperationResponseDto<TaskResponseDto> expectedResponse = new CrudOperationResponseDto<>();
        expectedResponse.setCode(200);
        expectedResponse.setResultMessage("OPERATION SUCCESSFUL");
        expectedResponse.setData(new TaskResponseDto());

        when(taskService.getTaskByCriteria(criteria, value)).thenReturn(expectedResponse);

        // Act
        CrudOperationResponseDto<TaskResponseDto> response = taskController.getTaskByCriteria(criteria, value);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getCode()),
                () -> assertEquals("OPERATION SUCCESSFUL", response.getResultMessage()),
                () -> assertNotNull(response.getData()),
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
        CrudOperationResponseDto<TaskResponseDto> expectedResponse = new CrudOperationResponseDto<>();
        expectedResponse.setCode(200);
        expectedResponse.setResultMessage("OPERATION SUCCESSFUL");
        expectedResponse.setData(new TaskResponseDto());

        when(taskService.getTaskByCriterias(criteria, value, criteria2, value2)).thenReturn(expectedResponse);

        // Act
        CrudOperationResponseDto<TaskResponseDto> response = taskController.getTaskByCriterias(criteria, value, criteria2, value2);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getCode()),
                () -> assertEquals("OPERATION SUCCESSFUL", response.getResultMessage()),
                () -> assertNotNull(response.getData()),
                () -> verify(taskService).getTaskByCriterias(criteria, value, criteria2, value2)
        );
    }

    @Test
    void saveTask_WhenValidTask_ShouldReturnSuccessResponse() {
        // Arrange
        Task task = new Task();
        CrudOperationResponseDto<TaskResponseDto> expectedResponse = new CrudOperationResponseDto<>();
        expectedResponse.setCode(201);
        expectedResponse.setResultMessage("OPERATION SUCCESSFUL");
        expectedResponse.setData(new TaskResponseDto());

        when(taskService.saveTask(task)).thenReturn(expectedResponse);

        // Act
        CrudOperationResponseDto<TaskResponseDto> response = taskController.saveTask(task);

        // Assert
        assertAll(
                () -> assertEquals(201, response.getCode()),
                () -> assertEquals("OPERATION SUCCESSFUL", response.getResultMessage()),
                () -> assertNotNull(response.getData()),
                () -> verify(taskService).saveTask(task)
        );
    }

    @Test
    void updateTask_WhenTaskExists_ShouldReturnSuccessResponse() {
        // Arrange
        long taskId = 1L;
        Task task = new Task();
        CrudOperationResponseDto<TaskResponseDto> expectedResponse = new CrudOperationResponseDto<>();
        expectedResponse.setCode(200);
        expectedResponse.setResultMessage("OPERATION SUCCESSFUL");
        expectedResponse.setData(new TaskResponseDto());

        when(taskService.updateTask(taskId, task)).thenReturn(expectedResponse);

        // Act
        CrudOperationResponseDto<TaskResponseDto> response = taskController.updateTask(taskId, task);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getCode()),
                () -> assertEquals("OPERATION SUCCESSFUL", response.getResultMessage()),
                () -> assertNotNull(response.getData()),
                () -> verify(taskService).updateTask(taskId, task)
        );
    }

    @Test
    void deleteTask_WhenTaskExists_ShouldReturnSuccessResponse() {
        // Arrange
        long taskId = 1L;
        CrudOperationResponseDto expectedResponse = new CrudOperationResponseDto<>();
        expectedResponse.setCode(200);
        expectedResponse.setResultMessage("OPERATION SUCCESSFUL");

        when(taskService.deleteTask(taskId)).thenReturn(expectedResponse);

        // Act
        CrudOperationResponseDto response = taskController.deleteTask(taskId);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getCode()),
                () -> assertEquals("OPERATION SUCCESSFUL", response.getResultMessage()),
                () -> verify(taskService).deleteTask(taskId)
        );
    }
}