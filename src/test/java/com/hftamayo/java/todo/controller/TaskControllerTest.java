package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.CrudOperationResponseDto;
import com.hftamayo.java.todo.dto.pagination.CursorPaginationDto;
import com.hftamayo.java.todo.dto.pagination.PageRequestDto;
import com.hftamayo.java.todo.dto.pagination.PageResponseDto;
import com.hftamayo.java.todo.dto.pagination.PaginatedDataDto;
import com.hftamayo.java.todo.dto.roles.RolesResponseDto;
import com.hftamayo.java.todo.dto.task.TaskResponseDto;
import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.entity.Task;
import com.hftamayo.java.todo.exceptions.ResourceNotFoundException;
import com.hftamayo.java.todo.exceptions.ValidationException;
import com.hftamayo.java.todo.services.TaskService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
        int page = 0;
        int size = 2;
        String sort = null;

        PaginatedDataDto<TaskResponseDto> paginatedData = getTasksResponseDtoPaginatedDataDto();

        when(taskService.getPaginatedTasks(any(PageRequestDto.class))).thenReturn(paginatedData);

        CrudOperationResponseDto<PaginatedDataDto<TaskResponseDto>> response = taskController.getTasks(page, size, sort);

        assertAll(
                () -> assertEquals(200, response.getCode()),
                () -> assertEquals("OPERATION_SUCCESS", response.getResultMessage()),
                () -> assertNotNull(response.getData()),
                () -> assertEquals(1, response.getData().getContent().size()),
                () -> verify(taskService).getPaginatedTasks(any(PageRequestDto.class))
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
    void getTask_WhenTaskNotFound_ShouldThrowResourceNotFoundException() {
        // Arrange
        long taskId = 999L;
        when(taskService.getTask(taskId)).thenThrow(new ResourceNotFoundException("Task not found with id: " + taskId));

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> taskController.getTask(taskId));

        assertEquals("Task not found with id: 999", exception.getMessage());
        verify(taskService).getTask(taskId);
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
    void getTaskByCriteria_WhenInvalidCriteria_ShouldThrowValidationException() {
        // Arrange
        String criteria = "invalid";
        String value = "pending";
        when(taskService.getTaskByCriteria(criteria, value)).thenThrow(new ValidationException("Invalid criteria: " + criteria));

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> taskController.getTaskByCriteria(criteria, value));

        assertEquals("Invalid criteria: invalid", exception.getMessage());
        verify(taskService).getTaskByCriteria(criteria, value);
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
    void saveTask_WhenInvalidTask_ShouldThrowValidationException() {
        // Arrange
        Task task = new Task();
        when(taskService.saveTask(task)).thenThrow(new ValidationException("Task title is required"));

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> taskController.saveTask(task));

        assertEquals("Task title is required", exception.getMessage());
        verify(taskService).saveTask(task);
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
    void updateTask_WhenTaskNotFound_ShouldThrowResourceNotFoundException() {
        // Arrange
        long taskId = 999L;
        Task task = new Task();
        when(taskService.updateTask(taskId, task)).thenThrow(new ResourceNotFoundException("Task not found with id: " + taskId));

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> taskController.updateTask(taskId, task));

        assertEquals("Task not found with id: 999", exception.getMessage());
        verify(taskService).updateTask(taskId, task);
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

    @Test
    void deleteTask_WhenTaskNotFound_ShouldThrowResourceNotFoundException() {
        // Arrange
        long taskId = 999L;
        when(taskService.deleteTask(taskId)).thenThrow(new ResourceNotFoundException("Task not found with id: " + taskId));

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> taskController.deleteTask(taskId));

        assertEquals("Task not found with id: 999", exception.getMessage());
        verify(taskService).deleteTask(taskId);
    }

    @Test
    void getTasks_WhenTasksExist_ShouldReturnPaginatedResponse() {
        PaginatedDataDto<TaskResponseDto> paginatedData = getTasksResponseDtoPaginatedDataDto();

        int page = 0;
        int size = 2;
        String sort = null;

        when(taskService.getPaginatedTasks(any(PageRequestDto.class))).thenReturn(paginatedData);

        CrudOperationResponseDto<PaginatedDataDto<TaskResponseDto>> response = taskController.getTasks(page, size, sort);

        ArgumentCaptor<PageRequestDto> pageRequestCaptor = ArgumentCaptor.forClass(PageRequestDto.class);

        verify(taskService).getPaginatedTasks(pageRequestCaptor.capture());

        PageRequestDto capturedRequest = pageRequestCaptor.getValue();
        PaginatedDataDto<TaskResponseDto> responseData = response.getData();

        assertAll(
                () -> assertEquals(1, responseData.getContent().size()),
                () -> assertEquals(0, responseData.getPagination().getCurrentPage()),
                () -> assertEquals(2, responseData.getPagination().getLimit()),
                () -> assertEquals(1, responseData.getPagination().getTotalCount()),
                () -> assertEquals(1, responseData.getPagination().getTotalPages()),
                () -> assertTrue(responseData.getPagination().isLastPage()),
                () -> assertEquals(page, capturedRequest.getPage()),
                () -> assertEquals(size, capturedRequest.getSize()),
                () -> assertEquals(sort, capturedRequest.getSort())
        );
    }

    @Test
    void getTasks_WhenNoRolesExist_ShouldReturnEmptyPaginatedResponse() {
        PaginatedDataDto<TaskResponseDto> paginatedData = new PaginatedDataDto<>();
        paginatedData.setContent(List.of());

        CursorPaginationDto pagination = new CursorPaginationDto();
        pagination.setCurrentPage(0);
        pagination.setLimit(2);
        pagination.setTotalCount(0);
        pagination.setTotalPages(0);
        pagination.setLastPage(true);
        paginatedData.setPagination(pagination);

        int page = 0;
        int size = 2;
        String sort = null;

        when(taskService.getPaginatedTasks(any(PageRequestDto.class))).thenReturn(paginatedData);

        CrudOperationResponseDto<PaginatedDataDto<TaskResponseDto>> response = taskController.getTasks(page, size, sort);

        ArgumentCaptor<PageRequestDto> pageRequestCaptor = ArgumentCaptor.forClass(PageRequestDto.class);

        verify(taskService).getPaginatedTasks(pageRequestCaptor.capture());

        PageRequestDto capturedRequest = pageRequestCaptor.getValue();
        PaginatedDataDto<TaskResponseDto> responseData = response.getData();

        assertAll(
                () -> assertEquals(0, responseData.getContent().size()),
                () -> assertEquals(0, responseData.getPagination().getTotalCount()),
                () -> assertEquals(0, responseData.getPagination().getTotalPages()),
                () -> assertTrue(responseData.getPagination().isLastPage()),
                () -> assertEquals(page, capturedRequest.getPage()),
                () -> assertEquals(size, capturedRequest.getSize()),
                () -> assertEquals(sort, capturedRequest.getSort())
        );
    }

    private static @NotNull PaginatedDataDto<TaskResponseDto> getTasksResponseDtoPaginatedDataDto() {
        PaginatedDataDto<TaskResponseDto> paginatedData = new PaginatedDataDto<>();
        paginatedData.setContent(List.of(new TaskResponseDto()));

        CursorPaginationDto pagination = new CursorPaginationDto();
        pagination.setCurrentPage(0);
        pagination.setLimit(2);
        pagination.setTotalCount(1);
        pagination.setTotalPages(1);
        pagination.setLastPage(true);
        paginatedData.setPagination(pagination);

        CrudOperationResponseDto<PaginatedDataDto<TaskResponseDto>> expectedResponse =
                new CrudOperationResponseDto<>(200, "OPERATION_SUCCESS", paginatedData);
        return paginatedData;
    }
}