package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dto.pagination.PageRequestDto;
import com.hftamayo.java.todo.dto.pagination.PaginatedDataDto;
import com.hftamayo.java.todo.dto.task.TaskResponseDto;
import com.hftamayo.java.todo.entity.Task;
import com.hftamayo.java.todo.mapper.TaskMapper;
import com.hftamayo.java.todo.repository.TaskRepository;
import com.hftamayo.java.todo.exceptions.ResourceNotFoundException;
import com.hftamayo.java.todo.exceptions.DuplicateResourceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void getTasks_WhenTasksExist_ShouldReturnListOfTasks() {
        // Arrange
        List<Task> tasksList = List.of(
                createTask(1L, "Task 1"),
                createTask(2L, "Task 2")
        );
        List<TaskResponseDto> responseDtos = List.of(
                createTaskDto(1L, "Task 1"),
                createTaskDto(2L, "Task 2")
        );

        when(taskRepository.findAll()).thenReturn(tasksList);
        when(taskMapper.taskToDto(tasksList.get(0))).thenReturn(responseDtos.get(0));
        when(taskMapper.taskToDto(tasksList.get(1))).thenReturn(responseDtos.get(1));

        // Act
        List<TaskResponseDto> result = taskService.getTasks();

        // Assert
        assertEquals(2, result.size());
        assertEquals(responseDtos, result);
        verify(taskRepository).findAll();
        verify(taskMapper, times(2)).taskToDto(any(Task.class));
    }

    @Test
    void getTasks_WhenNoTasksExist_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(taskRepository.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> taskService.getTasks());
        
        assertEquals("Task with identifier all not found", exception.getMessage());
        verify(taskRepository).findAll();
        verifyNoInteractions(taskMapper);
    }

    @Test
    void getTask_ById_WhenTaskExists_ShouldReturnTask() {
        // Arrange
        Long taskId = 1L;
        Task task = createTask(taskId, "Task 1");
        TaskResponseDto responseDto = createTaskDto(taskId, "Task 1");

        when(taskRepository.findTaskById(taskId)).thenReturn(Optional.of(task));
        when(taskMapper.taskToDto(task)).thenReturn(responseDto);

        // Act
        TaskResponseDto result = taskService.getTask(taskId);

        // Assert
        assertEquals(responseDto, result);
        verify(taskRepository).findTaskById(taskId);
        verify(taskMapper).taskToDto(task);
    }

    @Test
    void getTask_ById_WhenTaskDoesNotExist_ShouldThrowResourceNotFoundException() {
        // Arrange
        Long taskId = 1L;
        when(taskRepository.findTaskById(taskId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> taskService.getTask(taskId));
        
        assertEquals("Task with id " + taskId + " not found", exception.getMessage());
        verify(taskRepository).findTaskById(taskId);
        verifyNoInteractions(taskMapper);
    }

    @Test
    void getTask_ByCriteria_WhenTaskExists_ShouldReturnTask() {
        // Arrange
        String criteria = "title";
        String value = "Task 1";
        Task task = createTask(1L, "Task 1");
        TaskResponseDto responseDto = createTaskDto(1L, "Task 1");

        when(taskRepository.findAll((Specification<Task>) any())).thenReturn(List.of(task));
        when(taskMapper.taskToDto(task)).thenReturn(responseDto);

        // Act
        TaskResponseDto result = taskService.getTaskByCriteria(criteria, value);

        // Assert
        assertEquals(responseDto, result);
        verify(taskRepository).findAll((Specification<Task>) any());
        verify(taskMapper).taskToDto(task);
    }

    @Test
    void getTask_ByCriteria_WhenNoTasksExist_ShouldThrowResourceNotFoundException() {
        // Arrange
        String criteria = "title";
        String value = "Non-existent Task";
        when(taskRepository.findAll((Specification<Task>) any())).thenReturn(Collections.emptyList());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> taskService.getTaskByCriteria(criteria, value));
        
        assertEquals("Task with identifier title=Non-existent Task not found", exception.getMessage());
        verify(taskRepository).findAll((Specification<Task>) any());
        verifyNoInteractions(taskMapper);
    }

    @Test
    void getTask_ByCriterias_WhenTaskExists_ShouldReturnTask() {
        // Arrange
        String criteria = "title";
        String value = "Task 1";
        String criteria2 = "status";
        String value2 = "true";
        Task task = createTask(1L, "Task 1");
        TaskResponseDto responseDto = createTaskDto(1L, "Task 1");

        when(taskRepository.findAll((Specification<Task>) any())).thenReturn(List.of(task));
        when(taskMapper.taskToDto(task)).thenReturn(responseDto);

        // Act
        TaskResponseDto result = taskService.getTaskByCriterias(criteria, value, criteria2, value2);

        // Assert
        assertEquals(responseDto, result);
        verify(taskRepository).findAll((Specification<Task>) any());
        verify(taskMapper).taskToDto(task);
    }

    @Test
    void getTask_ByCriterias_WhenNoTasksExist_ShouldThrowResourceNotFoundException() {
        // Arrange
        String criteria = "title";
        String value = "Non-existent Task";
        String criteria2 = "status";
        String value2 = "true";
        when(taskRepository.findAll((Specification<Task>) any())).thenReturn(Collections.emptyList());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> taskService.getTaskByCriterias(criteria, value, criteria2, value2));
        
        assertEquals("Task with identifier title=Non-existent Task, status=true not found", exception.getMessage());
        verify(taskRepository).findAll((Specification<Task>) any());
        verifyNoInteractions(taskMapper);
    }

    @Test
    void getPaginatedTasks_WhenTasksExist_ShouldReturnPaginatedData() {
        // Arrange
        PageRequestDto pageRequestDto = new PageRequestDto(0, 2, null);
        List<Task> tasksList = List.of(
                createTask(1L, "Task 1"),
                createTask(2L, "Task 2")
        );
        List<TaskResponseDto> responseDtos = List.of(
                createTaskDto(1L, "Task 1"),
                createTaskDto(2L, "Task 2")
        );
        
        Page<Task> taskPage = new PageImpl<>(tasksList, PageRequest.of(0, 2), 2);
        
        when(taskRepository.findAll(any(PageRequest.class))).thenReturn(taskPage);
        when(taskMapper.toTaskResponseDto(tasksList.get(0))).thenReturn(responseDtos.get(0));
        when(taskMapper.toTaskResponseDto(tasksList.get(1))).thenReturn(responseDtos.get(1));

        // Act
        PaginatedDataDto<TaskResponseDto> result = taskService.getPaginatedTasks(pageRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(responseDtos, result.getContent());
        verify(taskRepository).findAll(any(PageRequest.class));
        verify(taskMapper, times(2)).toTaskResponseDto(any(Task.class));
    }

    @Test
    void saveTask_WhenValidTask_ShouldReturnSavedTask() {
        // Arrange
        Task newTask = createTask(null, "New Task");
        Task savedTask = createTask(1L, "New Task");
        TaskResponseDto responseDto = createTaskDto(1L, "New Task");

        when(taskRepository.findTaskByTitle("New Task")).thenReturn(Optional.empty());
        when(taskRepository.save(newTask)).thenReturn(savedTask);
        when(taskMapper.taskToDto(savedTask)).thenReturn(responseDto);

        // Act
        TaskResponseDto result = taskService.saveTask(newTask);

        // Assert
        assertEquals(responseDto, result);
        verify(taskRepository).findTaskByTitle("New Task");
        verify(taskRepository).save(newTask);
        verify(taskMapper).taskToDto(savedTask);
    }

    @Test
    void saveTask_WhenTaskWithSameTitleExists_ShouldThrowDuplicateResourceException() {
        // Arrange
        Task newTask = createTask(null, "Existing Task");
        Task existingTask = createTask(1L, "Existing Task");

        when(taskRepository.findTaskByTitle("Existing Task")).thenReturn(Optional.of(existingTask));

        // Act & Assert
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
                () -> taskService.saveTask(newTask));
        
        assertTrue(exception.getMessage().contains("title"));
        assertTrue(exception.getMessage().contains("Existing Task"));
        verify(taskRepository).findTaskByTitle("Existing Task");
        verify(taskRepository, never()).save(any(Task.class));
        verifyNoInteractions(taskMapper);
    }

    @Test
    void updateTask_WhenTaskExists_ShouldReturnUpdatedTask() {
        // Arrange
        Long taskId = 1L;
        Task existingTask = createTask(taskId, "Original Task");
        Task updatedTask = createTask(taskId, "Updated Task");
        TaskResponseDto responseDto = createTaskDto(taskId, "Updated Task");

        when(taskRepository.findTaskById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);
        when(taskMapper.taskToDto(updatedTask)).thenReturn(responseDto);

        // Act
        TaskResponseDto result = taskService.updateTask(taskId, updatedTask);

        // Assert
        assertEquals(responseDto, result);
        verify(taskRepository).findTaskById(taskId);
        verify(taskRepository).save(any(Task.class));
        verify(taskMapper).taskToDto(updatedTask);
    }

    @Test
    void updateTask_WhenTaskDoesNotExist_ShouldThrowResourceNotFoundException() {
        // Arrange
        Long taskId = 999L;
        Task updatedTask = createTask(taskId, "Updated Task");

        when(taskRepository.findTaskById(taskId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> taskService.updateTask(taskId, updatedTask));
        
        assertEquals("Task with id " + taskId + " not found", exception.getMessage());
        verify(taskRepository).findTaskById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
        verifyNoInteractions(taskMapper);
    }

    @Test
    void deleteTask_WhenTaskExists_ShouldDeleteTask() {
        // Arrange
        Long taskId = 1L;
        Task task = createTask(taskId, "Task to Delete");

        when(taskRepository.findTaskById(taskId)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).deleteTaskById(taskId);

        // Act
        assertDoesNotThrow(() -> taskService.deleteTask(taskId));

        // Assert
        verify(taskRepository).findTaskById(taskId);
        verify(taskRepository).deleteTaskById(taskId);
    }

    @Test
    void deleteTask_WhenTaskDoesNotExist_ShouldThrowResourceNotFoundException() {
        // Arrange
        Long taskId = 999L;
        when(taskRepository.findTaskById(taskId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> taskService.deleteTask(taskId));
        
        assertEquals("Task with id " + taskId + " not found", exception.getMessage());
        verify(taskRepository).findTaskById(taskId);
        verify(taskRepository, never()).deleteTaskById(any(Long.class));
    }

    // Helper methods
    private Task createTask(Long id, String title) {
        Task task = new Task();
        task.setId(id);
        task.setTitle(title);
        task.setDescription("Description for " + title);
        task.setStatus(true);
        return task;
    }

    private TaskResponseDto createTaskDto(Long id, String title) {
        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(id);
        dto.setTitle(title);
        dto.setDescription("Description for " + title);
        dto.setStatus(true);
        return dto;
    }
}