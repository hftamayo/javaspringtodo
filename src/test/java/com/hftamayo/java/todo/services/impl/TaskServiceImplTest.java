package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dto.CrudOperationResponseDto;
import com.hftamayo.java.todo.dto.task.TaskResponseDto;
import com.hftamayo.java.todo.mapper.TaskMapper;
import com.hftamayo.java.todo.entity.Task;
import com.hftamayo.java.todo.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
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
    void getTasks_WhenTasksExist_ShouldReturnSuccessResponse() {
        List<Task> tasksList = List.of(
                createTask(1L, "Task 1"),
                createTask(2L, "Task 2")
        );
        List<TaskResponseDto> responseDtos = List.of(
                createTaskDto(1L, "Task 1"),
                createTaskDto(2L, "Task 2")
        );

        when(taskRepository.findAll()).thenReturn(tasksList);
        when(taskMapper.taskToDto(any(Task.class)))
                .thenReturn(responseDtos.get(0), responseDtos.get(1));

        CrudOperationResponseDto<TaskResponseDto> result = taskService.getTasks();

        assertEquals(200, result.getCode());
        assertEquals("OPERATION SUCCESSFUL", result.getResultMessage());
        assertEquals(2, ((List<TaskResponseDto>)result.getDataList()).size());
        verify(taskRepository).findAll();
        verify(taskMapper, times(2)).taskToDto(any(Task.class));
    }

    @Test
    void getTasks_WhenNoTasksExist_ShouldReturnNotFoundResponse() {
        when(taskRepository.findAll()).thenReturn(Collections.emptyList());

        CrudOperationResponseDto<TaskResponseDto> result = taskService.getTasks();

        assertEquals(404, result.getCode());
        assertEquals("NO TASKS FOUND", result.getResultMessage());
        verify(taskRepository).findAll();
    }

    @Test
    void getTask_ById_WhenTaskExists_ShouldReturnSuccessResponse() {
        Long taskId = 1L;
        Task task = createTask(taskId, "Task 1");
        TaskResponseDto responseDto = createTaskDto(taskId, "Task 1");

        when(taskRepository.findTaskById(taskId)).thenReturn(Optional.of(task));
        when(taskMapper.taskToDto(task)).thenReturn(responseDto);

        CrudOperationResponseDto<TaskResponseDto> result = taskService.getTask(taskId);

        assertEquals(200, result.getCode());
        assertEquals("OPERATION SUCCESSFUL", result.getResultMessage());
        assertEquals(responseDto, result.getData());
        verify(taskRepository).findTaskById(taskId);
        verify(taskMapper).taskToDto(task);
    }

    @Test
    void getTask_ById_WhenTaskDoesNotExist_ShouldReturnNotFoundResponse() {
        Long taskId = 1L;

        when(taskRepository.findTaskById(taskId)).thenReturn(Optional.empty());

        CrudOperationResponseDto<TaskResponseDto> result = taskService.getTask(taskId);

        assertEquals(404, result.getCode());
        assertEquals("TASK NOT FOUND", result.getResultMessage());
        verify(taskRepository).findTaskById(taskId);
    }

    @Test
    void getTask_ByCriteria_WhenTaskExists_ShouldReturnSuccessResponse() {
        String criteria = "title";
        String value = "Task 1";
        Task task = createTask(1L, "Task 1");
        TaskResponseDto responseDto = createTaskDto(1L, "Task 1");

        when(taskRepository.findAll((Specification<Task>) any())).thenReturn(List.of(task));
        when(taskMapper.taskToDto(task)).thenReturn(responseDto);

        CrudOperationResponseDto<TaskResponseDto> result = taskService.getTaskByCriteria(criteria, value);

        assertEquals(200, result.getCode());
        assertEquals("OPERATION SUCCESSFUL", result.getResultMessage());
        assertEquals(List.of(responseDto), result.getDataList());
        verify(taskRepository).findAll((Specification<Task>) any());
        verify(taskMapper).taskToDto(task);
    }

    @Test
    void getTask_ByCriteria_WhenNoTasksExist_ShouldReturnNotFoundResponse() {
        String criteria = "title";
        String value = "Task 1";

        when(taskRepository.findAll((Specification<Task>) any())).thenReturn(Collections.emptyList());

        CrudOperationResponseDto<TaskResponseDto> result = taskService.getTaskByCriteria(criteria, value);

        assertEquals(404, result.getCode());
        assertEquals("NO TASKS FOUND", result.getResultMessage());
        verify(taskRepository).findAll((Specification<Task>) any());
    }

    @Test
    void getTask_ByCriterias_WhenTaskExists_ShouldReturnSuccessResponse() {
        String criteria = "title";
        String value = "Task 1";
        String criteria2 = "status";
        String value2 = "completed";
        Task task = createTask(1L, "Task 1");
        TaskResponseDto responseDto = createTaskDto(1L, "Task 1");

        when(taskRepository.findAll((Specification<Task>) any())).thenReturn(List.of(task));
        when(taskMapper.taskToDto(task)).thenReturn(responseDto);

        CrudOperationResponseDto<TaskResponseDto> result = taskService.getTaskByCriterias(criteria, value, criteria2, value2);

        assertEquals(200, result.getCode());
        assertEquals("OPERATION SUCCESSFUL", result.getResultMessage());
        assertEquals(List.of(responseDto), result.getDataList());
        verify(taskRepository).findAll((Specification<Task>) any());
        verify(taskMapper).taskToDto(task);
    }

    @Test
    void getTask_ByCriterias_WhenNoTasksExist_ShouldReturnNotFoundResponse() {
        String criteria = "title";
        String value = "Task 1";
        String criteria2 = "status";
        String value2 = "completed";

        when(taskRepository.findAll((Specification<Task>) any())).thenReturn(Collections.emptyList());

        CrudOperationResponseDto<TaskResponseDto> result = taskService.getTaskByCriterias(criteria, value, criteria2, value2);

        assertEquals(404, result.getCode());
        assertEquals("NO TASKS FOUND", result.getResultMessage());
        verify(taskRepository).findAll((Specification<Task>) any());
    }

    @Test
    void saveTask_WhenTaskIsValid_ShouldReturnSuccessResponse() {
        Task task = createTask(1L, "Task 1");
        TaskResponseDto taskDto = createTaskDto(1L, "Task 1");

        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.taskToDto(task)).thenReturn(taskDto);

        CrudOperationResponseDto<TaskResponseDto> result = taskService.saveTask(task);

        assertEquals(201, result.getCode());
        assertEquals("OPERATION SUCCESSFUL", result.getResultMessage());
        assertEquals(taskDto, result.getData());
        verify(taskRepository).save(task);
        verify(taskMapper).taskToDto(task);
    }

    @Test
    void saveTask_WhenTaskAlreadyExists_ShouldReturnConflictResponse() {
        Task existingTask = createTask(1L, "Task 1");

        when(taskRepository.findTaskByTitle(existingTask.getTitle())).thenReturn(Optional.of(existingTask));

        CrudOperationResponseDto<TaskResponseDto> result = taskService.saveTask(existingTask);

        assertEquals(400, result.getCode());
        assertEquals("TASK ALREADY EXISTS", result.getResultMessage());
        verify(taskRepository).findTaskByTitle(existingTask.getTitle());
        verify(taskRepository, never()).save(any(Task.class));
        verify(taskMapper, never()).taskToDto(any(Task.class));
    }

    @Test
    void updateTask_WhenTaskExists_ShouldReturnSuccessResponse() {
        Long taskId = 1L;
        Task existingTask = createTask(taskId, "Task 1");
        Task updatedTask = createTask(taskId, "Updated Task 1");
        TaskResponseDto taskDto = createTaskDto(taskId, "Updated Task 1");

        when(taskRepository.findTaskById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(updatedTask)).thenReturn(updatedTask);
        when(taskMapper.taskToDto(updatedTask)).thenReturn(taskDto);

        CrudOperationResponseDto<TaskResponseDto> result = taskService.updateTask(taskId, updatedTask);

        assertEquals(200, result.getCode());
        assertEquals("OPERATION SUCCESSFUL", result.getResultMessage());
        assertEquals(taskDto, result.getData());
        verify(taskRepository).findTaskById(taskId);
        verify(taskRepository).save(updatedTask);
        verify(taskMapper).taskToDto(updatedTask);
    }

    @Test
    void updateTask_WhenTaskDoesNotExist_ShouldReturnNotFoundResponse() {
        Long taskId = 1L;
        Task updatedTask = createTask(taskId, "Updated Task 1");

        when(taskRepository.findTaskById(taskId)).thenReturn(Optional.empty());

        CrudOperationResponseDto<TaskResponseDto> result = taskService.updateTask(taskId, updatedTask);

        assertEquals(404, result.getCode());
        assertEquals("TASK NOT FOUND", result.getResultMessage());
        verify(taskRepository).findTaskById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
        verify(taskMapper, never()).taskToDto(any(Task.class));
    }

    @Test
    void deleteTask_WhenTaskExists_ShouldReturnSuccessResponse() {
        Long taskId = 1L;
        Task existingTask = createTask(taskId, "Task 1");

        when(taskRepository.findTaskById(taskId)).thenReturn(Optional.of(existingTask));

        CrudOperationResponseDto result = taskService.deleteTask(taskId);

        assertEquals(200, result.getCode());
        assertEquals("OPERATION SUCCESSFUL", result.getResultMessage());
        verify(taskRepository).findTaskById(taskId);
        verify(taskRepository).deleteTaskById(taskId);
    }

    @Test
    void deleteTask_WhenTaskDoesNotExist_ShouldReturnNotFoundResponse() {
        Long taskId = 1L;

        when(taskRepository.findTaskById(taskId)).thenReturn(Optional.empty());

        CrudOperationResponseDto result = taskService.deleteTask(taskId);

        assertEquals(404, result.getCode());
        assertEquals("TASK NOT FOUND", result.getResultMessage());
        verify(taskRepository).findTaskById(taskId);
        verify(taskRepository, never()).deleteTaskById(any(Long.class));
    }

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