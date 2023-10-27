package com.hftamayo.java.todo.Controller;

import com.hftamayo.java.todo.Model.Task;
import com.hftamayo.java.todo.Services.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TodoControllerTest {

    @Mock
    private TodoService todoService;

    @InjectMocks
    private TodoController todoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTasks() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1L, "Task 1", "Description 1", LocalDateTime.now(), LocalDateTime.now(), true));
        tasks.add(new Task(2L, "Task 2", "Description 2", LocalDateTime.now(), LocalDateTime.now(), false));
        when(todoService.getTasks()).thenReturn(tasks);

        List<Task> response = todoController.getTasks();

        assertEquals(2, response.size());
        assertEquals("Task 1", response.get(0).getTitle());
        assertEquals("Task 2", response.get(1).getTitle());
    }

    @Test
    void testGetTask() {
        // Arrange
        long taskId = 1L;
        Task task = new Task(taskId, "Task 1", "Description 1", LocalDateTime.now(), LocalDateTime.now(), true);
        when(todoService.getTaskById(taskId)).thenReturn(task);

        // Act
        Task response = todoController.getTask(taskId);

        // Assert
        assertEquals("Task 1", response.getTitle());
    }

    @Test
    void testSaveTask() {
        // Arrange
        Task task = new Task(1L, "New Task", "Description", LocalDateTime.now(), LocalDateTime.now(), true);
        when(todoService.saveTask(task)).thenReturn(task);

        // Act
        Task response = todoController.saveTask(task);

        // Assert
        assertEquals("New Task", response.getTitle());
    }

    @Test
    void testUpdateTask() {
        // Arrange
        long taskId = 1L;
        Task updatedTask = new Task(taskId, "Updated Task", "Updated Description", LocalDateTime.now(), LocalDateTime.now(),false);
        when(todoService.updateTask(taskId, updatedTask)).thenReturn(updatedTask);

        // Act
        ResponseEntity<Task> responseEntity = todoController.updateTask(taskId, updatedTask);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Updated Task", responseEntity.getBody().getTitle());
    }

    @Test
    void testDeleteTask() {
        // Arrange
        long taskId = 1L;

        // Act
        String response = todoController.deleteTask(taskId);

        // Assert
        assertEquals("data deleted", response);
        verify(todoService, times(1)).deleteTask(taskId);
    }
}
