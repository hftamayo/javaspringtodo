package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dao.RolesDao;
import com.hftamayo.java.todo.dao.TaskDao;
import com.hftamayo.java.todo.dto.task.TaskResponseDto;
import com.hftamayo.java.todo.exceptions.EntityAlreadyExistsException;
import com.hftamayo.java.todo.model.Task;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaskServiceImplTest {

    private TaskServiceImpl taskService;
    private TaskDao taskDao;

    @BeforeEach
    public void setUp() {
        taskDao = Mockito.mock(TaskDao.class);
        taskService = new TaskServiceImpl(taskDao);
    }

    @Test
    public void testGetTasks() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(true);
        task.setDateAdded(LocalDateTime.now());

        when(taskDao.getTasks()).thenReturn(Collections.singletonList(task));

        List<TaskResponseDto> tasks = taskService.getTasks();
        assertFalse(tasks.isEmpty());
        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getTitle());
    }

    @Test
    public void testGetTask() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(true);
        task.setDateAdded(LocalDateTime.now());

        when(taskDao.getTaskById(1L)).thenReturn(Optional.of(task));

        Optional<TaskResponseDto> foundTask = taskService.getTask(1L);
        assertTrue(foundTask.isPresent());
        assertEquals("Test Task", foundTask.get().getTitle());
    }

    @Test
    public void testSaveTask() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(true);
        task.setDateAdded(LocalDateTime.now());

        when(taskDao.getTaskByTitle("Test Task")).thenReturn(Optional.empty());
        when(taskDao.saveTask(any(Task.class))).thenReturn(task);

        TaskResponseDto savedTask = taskService.saveTask(task);
        assertEquals("Test Task", savedTask.getTitle());
    }

    @Test
    public void testSaveTaskAlreadyExists() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(true);
        task.setDateAdded(LocalDateTime.now());

        when(taskDao.getTaskByTitle("Test Task")).thenReturn(Optional.of(task));

        assertThrows(EntityAlreadyExistsException.class, () -> taskService.saveTask(task));
    }

    @Test
    public void testUpdateTask() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(true);
        task.setDateAdded(LocalDateTime.now());

        when(taskDao.getTaskById(1L)).thenReturn(Optional.of(task));
        when(taskDao.updateTask(anyLong(), anyMap())).thenReturn(task);

        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("Updated Description");
        updatedTask.setStatus(false);

        TaskResponseDto updatedTaskDto = taskService.updateTask(1L, updatedTask);
        assertEquals("Updated Task", updatedTaskDto.getTitle());
        assertEquals("Updated Description", updatedTaskDto.getDescription());
    }

    @Test
    public void testUpdateTaskNotFound() {
        when(taskDao.getTaskById(1L)).thenReturn(Optional.empty());

        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("Updated Description");
        updatedTask.setStatus(false);

        assertThrows(EntityNotFoundException.class, () -> taskService.updateTask(1L, updatedTask));
    }

    @Test
    public void testDeleteTask() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(true);
        task.setDateAdded(LocalDateTime.now());

        when(taskDao.getTaskById(1L)).thenReturn(Optional.of(task));
        doNothing().when(taskDao).deleteTask(1L);

        taskService.deleteTask(1L);
        verify(taskDao, times(1)).deleteTask(1L);
    }

    @Test
    public void testDeleteTaskNotFound() {
        when(taskDao.getTaskById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.deleteTask(1L));
    }
}