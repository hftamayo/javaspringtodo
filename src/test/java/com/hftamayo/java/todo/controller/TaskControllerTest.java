package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.task.TaskResponseDto;
import com.hftamayo.java.todo.model.Task;
import com.hftamayo.java.todo.services.TaskService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskControllerTest {

    private TaskController taskController;
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        taskService = Mockito.mock(TaskService.class);
        taskController = new TaskController(taskService);
    }

    @Test
    public void testGetTasks() {
        TaskResponseDto task = new TaskResponseDto();
        when(taskService.getTasks()).thenReturn(Collections.singletonList(task));

        List<TaskResponseDto> tasks = taskController.getTasks();
        assertEquals(1, tasks.size());
        verify(taskService, times(1)).getTasks();
    }

    @Test
    public void testGetTask() {
        TaskResponseDto task = new TaskResponseDto();
        when(taskService.getTask(1L)).thenReturn(Optional.of(task));

        Optional<TaskResponseDto> result = taskController.getTask(1L);
        assertTrue(result.isPresent());
        verify(taskService, times(1)).getTask(1L);
    }

    @Test
    public void testGetTaskByCriteria() {
        TaskResponseDto task = new TaskResponseDto();
        when(taskService.getTaskByCriteria("name", "Test Task"))
                .thenReturn(Optional.of(Collections.singletonList(task)));

        Optional<List<TaskResponseDto>> result = taskController
                .getTaskByCriteria("name", "Test Task");
        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        verify(taskService, times(1))
                .getTaskByCriteria("name", "Test Task");
    }

    @Test
    public void testGetTaskByCriterias() {
        TaskResponseDto task = new TaskResponseDto();
        when(taskService.getTaskByCriterias("name", "Test Task", "status", "active"))
                .thenReturn(Optional.of(Collections.singletonList(task)));

        Optional<List<TaskResponseDto>> result = taskController
                .getTaskByCriterias("name", "Test Task", "status", "active");
        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        verify(taskService, times(1))
                .getTaskByCriterias("name", "Test Task", "status", "active");
    }

    @Test
    public void testSaveTask() {
        Task task = new Task();
        TaskResponseDto taskResponse = new TaskResponseDto();
        when(taskService.saveTask(task)).thenReturn(taskResponse);

        TaskResponseDto result = taskController.saveTask(task);
        assertEquals(taskResponse, result);
        verify(taskService, times(1)).saveTask(task);
    }

    @Test
    public void testUpdateTask_Success() {
        Task task = new Task();
        TaskResponseDto taskResponse = new TaskResponseDto();
        when(taskService.updateTask(1L, task)).thenReturn(taskResponse);

        TaskResponseDto result = taskController.updateTask(1L, task);
        assertEquals(taskResponse, result);
        verify(taskService, times(1)).updateTask(1L, task);
    }

    @Test
    public void testUpdateTask_NotFound() {
        Task task = new Task();
        when(taskService.updateTask(1L, task)).thenThrow(new EntityNotFoundException());

        assertThrows(ResponseStatusException.class, () -> taskController.updateTask(1L, task));
        verify(taskService, times(1)).updateTask(1L, task);
    }

    @Test
    public void testDeleteTask_Success() {
        doNothing().when(taskService).deleteTask(1L);

        ResponseEntity<?> response = taskController.deleteTask(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(taskService, times(1)).deleteTask(1L);
    }

    @Test
    public void testDeleteTask_NotFound() {
        doThrow(new EntityNotFoundException()).when(taskService).deleteTask(1L);

        ResponseEntity<?> response = taskController.deleteTask(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(taskService, times(1)).deleteTask(1L);
    }

    @Test
    public void testDeleteTask_InternalServerError() {
        doThrow(new RuntimeException()).when(taskService).deleteTask(1L);

        ResponseEntity<?> response = taskController.deleteTask(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(taskService, times(1)).deleteTask(1L);
    }
}