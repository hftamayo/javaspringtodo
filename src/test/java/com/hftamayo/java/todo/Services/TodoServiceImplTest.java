package com.hftamayo.java.todo.Services;

import com.hftamayo.java.todo.Model.Task;
import com.hftamayo.java.todo.Repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoServiceImplTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoServiceImpl todoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTasks() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1L, "Task 1", "Description 1", LocalDateTime.now(), LocalDateTime.now(), true));
        tasks.add(new Task(2L, "Task 2", "Description 2", LocalDateTime.now(), LocalDateTime.now(), false));
        when(todoRepository.findAll()).thenReturn(tasks);

        List<Task> retrievedTasks = todoService.getTasks();

        assertEquals(2, retrievedTasks.size());
        assertEquals("Task 1", retrievedTasks.get(0).getTitle());
        assertEquals("Task 2", retrievedTasks.get(1).getTitle());
    }

    @Test
    void testGetTaskById() {
        // Arrange
        long taskId = 1L;
        Task task = new Task(taskId, "Task 1", "Description 1", LocalDateTime.now(), LocalDateTime.now(), true);
        when(todoRepository.findById(taskId)).thenReturn(Optional.of(task));

        Task retrievedTask = todoService.getTaskById(taskId);

        assertNotNull(retrievedTask);
        assertEquals("Task 1", retrievedTask.getTitle());
    }

    @Test
    void testSaveTask() {
        Task newTask = new Task(1L, "Task 1", "Description 1", LocalDateTime.now(), LocalDateTime.now(), true);
        when(todoRepository.findByTitle("New Task")).thenReturn(null);
        when(todoRepository.save(newTask)).thenReturn(newTask);

        Task savedTask = todoService.saveTask(newTask);

        assertNotNull(savedTask);
        assertEquals("Task 1", savedTask.getTitle());
    }

    @Test
    void testUpdateTask() {
        long taskId = 1L;
        Task existingTask = new Task(taskId, "Task 1", "Description 1", LocalDateTime.now(), LocalDateTime.now(), true);
        Task updatedTask = new Task(taskId, "Updated Task", "Updated Description", LocalDateTime.now(), LocalDateTime.now(), false);
        when(todoRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(todoRepository.save(existingTask)).thenReturn(updatedTask);

        Task modifiedTask = todoService.updateTask(taskId, updatedTask);

        assertNotNull(modifiedTask);
        assertEquals("Updated Task", modifiedTask.getTitle());
        assertEquals("Updated Description", modifiedTask.getDescription());
        assertFalse(modifiedTask.isStatus());
    }

    @Test
    void testDeleteTask() {
        long taskId = 1L;
        when(todoRepository.existsById(taskId)).thenReturn(true);

        todoService.deleteTask(taskId);

        verify(todoRepository, times(1)).deleteById(taskId);
    }

}