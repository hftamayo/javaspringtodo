package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dto.CrudOperationResponseDto;
import com.hftamayo.java.todo.dto.pagination.PageRequestDto;
import com.hftamayo.java.todo.dto.pagination.PageResponseDto;
import com.hftamayo.java.todo.dto.roles.RolesResponseDto;
import com.hftamayo.java.todo.dto.task.TaskResponseDto;
import com.hftamayo.java.todo.mapper.TaskMapper;
import com.hftamayo.java.todo.entity.Task;
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
        List<Task> tasksList = List.of(
                createTask(1L, "Task 1"),
                createTask(2L, "Task 2")
        );
        List<TaskResponseDto> responseDtos = List.of(
                createTaskDto(1L, "Task 1"),
                createTaskDto(2L, "Task 2")
        );

        when(taskRepository.findAll()).thenReturn(tasksList);
        when(taskMapper.toTaskResponseDto(tasksList.get(0))).thenReturn(responseDtos.get(0));
        when(taskMapper.toTaskResponseDto(tasksList.get(1))).thenReturn(responseDtos.get(1));

        List<TaskResponseDto> result = taskService.getTasks().getDataList();

        assertEquals(2, result.size());
        assertEquals(responseDtos, result);
        verify(taskRepository).findAll();
        verify(taskMapper, times(2)).toTaskResponseDto(any(Task.class));
    }

    @Test
    void getTasks_WhenNoTasksExist_ShouldThrowResourceNotFoundException() {
        when(taskRepository.findAll()).thenReturn(Collections.emptyList());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> taskService.getTasks());
        
        assertEquals("Task with identifier all not found", exception.getMessage());
        verify(taskRepository).findAll();
        verifyNoInteractions(taskMapper);
    }

    @Test
    void getTask_ById_WhenTaskExists_ShouldReturnTask() {
        Long taskId = 1L;
        Task task = createTask(taskId, "Task 1");
        TaskResponseDto responseDto = createTaskDto(taskId, "Task 1");

        when(taskRepository.findTaskById(taskId)).thenReturn(Optional.of(task));
        when(taskMapper.taskToDto(task)).thenReturn(responseDto);

        TaskResponseDto result = taskService.getTask(taskId).getData();

        assertEquals(responseDto, result);
        verify(taskRepository).findTaskById(taskId);
        verify(taskMapper).toTaskResponseDto(task);
    }

    @Test
    void getTask_ById_WhenTaskDoesNotExist_ShouldThrowResourceNotFoundException() {
        Long taskId = 1L;

        when(taskRepository.findTaskById(taskId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> taskService.getTask(taskId));
        
        assertEquals("Task with id " + taskId + " not found", exception.getMessage());
        verify(taskRepository).findTaskById(taskId);
        verifyNoInteractions(taskMapper);
    }

    @Test
    void getTask_ByCriteria_WhenTaskExists_ShouldReturnTask() {
        String criteria = "title";
        String value = "Task 1";
        Task task = createTask(1L, "Task 1");
        TaskResponseDto responseDto = createTaskDto(1L, "Task 1");

        when(taskRepository.findAll((Specification<Task>) any())).thenReturn(List.of(task));
        when(taskMapper.taskToDto(task)).thenReturn(responseDto);

        List<TaskResponseDto> result = taskService.getTaskByCriteria(criteria, value).getDataList();

        assertEquals(List.of(responseDto), result);
        verify(taskRepository).findAll((Specification<Task>) any());
        verify(taskMapper).toTaskResponseDto(task);
    }

    @Test
    void getTask_ByCriteria_WhenNoTasksExist_ShouldThrowResourceNotFoundException() {
        String criteria = "title";
        String value = "Task 1";

        when(taskRepository.findAll((Specification<Task>) any())).thenReturn(Collections.emptyList());

        CrudOperationResponseDto<TaskResponseDto> response = taskService.getTaskByCriteria(criteria, value);

        assertEquals(404, response.getCode());
        assertEquals("NO TASKS FOUND", response.getResultMessage());
        verify(taskRepository).findAll((Specification<Task>) any());
    }

    @Test
    void getTask_ByCriterias_WhenTaskExists_ShouldReturnTask() {
        String criteria = "title";
        String value = "Task 1";
        String criteria2 = "status";
        String value2 = "completed";
        Task task = createTask(1L, "Task 1");
        TaskResponseDto responseDto = createTaskDto(1L, "Task 1");

        when(taskRepository.findAll((Specification<Task>) any())).thenReturn(List.of(task));
        when(taskMapper.toTaskResponseDto(task)).thenReturn(responseDto);

        List<TaskResponseDto> result = taskService
                .getTaskByCriterias(criteria, value, criteria2, value2).getDataList();

        assertEquals(List.of(responseDto), result);
        verify(taskRepository).findAll((Specification<Task>) any());
        verify(taskMapper).toTaskResponseDto(task);
    }

    @Test
    void getTask_ByCriterias_WhenNoTasksExist_ShouldThrowResourceNotFoundException() {
        String criteria = "title";
        String value = "Task 1";
        String criteria2 = "status";
        String value2 = "completed";

        when(taskRepository.findAll((Specification<Task>) any())).thenReturn(Collections.emptyList());

        CrudOperationResponseDto<TaskResponseDto> response = taskService.getTaskByCriteria(criteria, value);

        assertEquals(404, response.getCode());
        assertEquals("NO TASKS FOUND", response.getResultMessage());
        verify(taskRepository).findAll((Specification<Task>) any());
        verifyNoInteractions(taskMapper);
    }

        @Test
    void saveTask_WhenTaskIsValid_ShouldReturnSavedTask() {
        Task task = createTask(1L, "Task 1");
        TaskResponseDto taskDto = createTaskDto(1L, "Task 1");

        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.taskToDto(task)).thenReturn(taskDto);

        TaskResponseDto result = taskService.saveTask(task).getData();

        assertEquals(taskDto, result);
        verify(taskRepository).save(task);
        verify(taskMapper).toTaskResponseDto(task);
    }

    @Test
    void saveTask_WhenTaskAlreadyExists_ShouldThrowResourceAlreadyExistsException() {
        Task existingTask = createTask(1L, "Task 1");

        when(taskRepository.findTaskByTitle(existingTask.getTitle())).thenReturn(Optional.of(existingTask));

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
                () -> taskService.saveTask(existingTask));
        
        assertEquals("Resource with title '" + existingTask.getTitle() + "' already exists", exception.getMessage());
        verify(taskRepository).findTaskByTitle(existingTask.getTitle());
        verify(taskRepository, never()).save(any(Task.class));
        verify(taskMapper, never()).toTaskResponseDto(any(Task.class));
        verifyNoMoreInteractions(taskRepository);
        verifyNoInteractions(taskMapper);
    }

    @Test
    void updateTask_WhenTaskExists_ShouldReturnUpdatedTask() {
        Long taskId = 1L;
        Task existingTask = createTask(taskId, "Task 1");
        Task updatedTask = createTask(taskId, "Updated Task 1");
        TaskResponseDto taskDto = createTaskDto(taskId, "Updated Task 1");

        when(taskRepository.findTaskById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(updatedTask)).thenReturn(updatedTask);
        when(taskMapper.toTaskResponseDto(updatedTask)).thenReturn(taskDto);

        TaskResponseDto result = taskService.updateTask(taskId, updatedTask).getData();

        assertEquals(taskDto, result);
        verify(taskRepository).findTaskById(taskId);
        verify(taskRepository).save(updatedTask);
        verify(taskMapper).toTaskResponseDto(updatedTask);
    }

    @Test
    void updateTask_WhenTaskDoesNotExist_ShouldThrowResourceNotFoundException() {
        Long taskId = 1L;
        Task updatedTask = createTask(taskId, "Updated Task 1");

        when(taskRepository.findTaskById(taskId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> taskService.updateTask(taskId, updatedTask));
        
        assertEquals("Task with id " + taskId + " not found", exception.getMessage());
        verify(taskRepository).findTaskById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
        verify(taskMapper, never()).toTaskResponseDto(any(Task.class));
        verifyNoMoreInteractions(taskRepository);
        verifyNoInteractions(taskMapper);
    }

    @Test
    void deleteTask_WhenTaskExists_ShouldDeleteTask() {
        Long taskId = 1L;
        Task existingTask = createTask(taskId, "Task 1");

        when(taskRepository.findTaskById(taskId)).thenReturn(Optional.of(existingTask));

        taskService.deleteTask(taskId);

        verify(taskRepository).findTaskById(taskId);
        verify(taskRepository).deleteTaskById(taskId);
    }

    @Test
    void deleteTask_WhenTaskDoesNotExist_ShouldThrowResourceNotFoundException() {
        Long taskId = 1L;

        when(taskRepository.findTaskById(taskId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> taskService.deleteTask(taskId));
        
        assertEquals("Task with id " + taskId + " not found", exception.getMessage());
        verify(taskRepository).findTaskById(taskId);
        verify(taskRepository, never()).deleteTaskById(any(Long.class));
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void getPaginatedTasks_WhenTasksExist_ShouldReturnPaginatedTasks() {
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

        PageRequestDto pageRequestDto = new PageRequestDto(0, 2, null);

        PageResponseDto<RolesResponseDto> result = taskService.getPaginatedTasks(pageRequestDto);

        assertEquals(2, result.getContent().size());
        assertEquals(responseDtos, result.getContent());
        assertEquals(0, result.getPage());
        assertEquals(2, result.getSize());
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertTrue(result.isLast());
        verify(taskRepository).findAll(any(PageRequest.class));
        verify(taskMapper, times(2)).toTaskResponseDto(any(Task.class));
    }

    @Test
    void getPaginatedRoles_WhenMultiplePages_ShouldReturnFirstPageWithCorrectMetadata() {
        // Arrange
        List<Task> firstPageTask = List.of(
                createTask(1L, "Task 1"),
                createTask(2L, "Task 2")
        );
        List<TaskResponseDto> firstPageDtos = List.of(
                createTaskDto(1L, "Task 1"),
                createTaskDto(2L, "Task 2")
        );

        // Creamos una página con 2 elementos, pero indicamos que hay 5 elementos en total
        // Esto significa que habrá 3 páginas en total (5 elementos / 2 por página = 3 páginas)
        Page<Task> taskPage = new PageImpl<>(firstPageTask, PageRequest.of(0, 2), 5);

        // Configuramos los mocks
        when(taskRepository.findAll(any(PageRequest.class))).thenReturn(taskPage);
        when(taskMapper.toTaskResponseDto(firstPageTask.get(0))).thenReturn(firstPageDtos.get(0));
        when(taskMapper.toTaskResponseDto(firstPageTask.get(1))).thenReturn(firstPageDtos.get(1));

        PageRequestDto pageRequestDto = new PageRequestDto(0, 2, null);

        // Act
        PageResponseDto<RolesResponseDto> result = taskService.getPaginatedTasks(pageRequestDto);

        // Assert
        assertEquals(2, result.getContent().size());
        assertEquals(0, result.getPage()); // Primera página (índice 0)
        assertEquals(2, result.getSize()); // 2 elementos por página
        assertEquals(5, result.getTotalElements()); // 5 elementos en total
        assertEquals(3, result.getTotalPages()); // 3 páginas en total
        assertFalse(result.isLast()); // No es la última página
        assertEquals(firstPageDtos, result.getContent());

        verify(taskRepository).findAll(any(PageRequest.class));
        verify(taskMapper, times(2)).toTaskResponseDto(any(Task.class));
    }

    @Test
    void getPaginatedTask_WhenOnLastPage_ShouldIndicateIsLastPage() {
        // Arrange
        List<Task> lastPageTask = List.of(
                createTask(1L, "Task 1"),
        );
        List<TaskResponseDto> lastPageDtos = List.of(
                createTaskDto(1L, "Task 1"),
        );

        // Creamos la última página (índice 2) con 1 elemento, de un total de 5 elementos
        Page<Task> taskPage = new PageImpl<>(lastPageTask, PageRequest.of(2, 2), 5);

        when(taskRepository.findAll(any(PageRequest.class))).thenReturn(taskPage);
        when(taskMapper.toTaskResponseDto(lastPageTask.get(0))).thenReturn(lastPageDtos.get(0));

        PageRequestDto pageRequestDto = new PageRequestDto(2, 2, null);

        // Act
        PageResponseDto<TaskResponseDto> result = taskService.getPaginatedTasks(pageRequestDto);

        // Assert
        assertEquals(1, result.getContent().size()); // Solo 1 elemento en la última página
        assertEquals(2, result.getPage()); // Tercera página (índice 2)
        assertEquals(2, result.getSize());
        assertEquals(5, result.getTotalElements());
        assertEquals(3, result.getTotalPages());
        assertTrue(result.isLast()); // Es la última página

        verify(taskRepository).findAll(any(PageRequest.class));
        verify(taskMapper).toTaskResponseDto(any(Task.class));
    }

    @Test
    void getPaginatedTask_WhenNoRolesExist_ShouldReturnEmptyPage() {
        List<Task> taskList = List.of();
        Page<Task> taskPage = new PageImpl<>(taskList, PageRequest.of(0, 2), 0);

        when(taskRepository.findAll(any(PageRequest.class))).thenReturn(taskPage);

        PageRequestDto pageRequestDto = new PageRequestDto(0, 2, null);

        PageResponseDto<TaskResponseDto> result = taskService.getPaginatedTasks(pageRequestDto);

        assertEquals(0, result.getContent().size());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
        assertTrue(result.isLast());
        verify(taskRepository).findAll(any(PageRequest.class));
        verifyNoInteractions(taskMapper);
    }

    ///

    // Helper methods to create test data
    private Task createTask(Long id, String title) {
        Task task = new Task();
        task.setId(id);
        task.setTitle(title);
        return task;
    }

    private TaskResponseDto createTaskDto(Long id, String title) {
        TaskResponseDto taskDto = new TaskResponseDto();
        taskDto.setId(id);
        taskDto.setTitle(title);
        return taskDto;
    }
}