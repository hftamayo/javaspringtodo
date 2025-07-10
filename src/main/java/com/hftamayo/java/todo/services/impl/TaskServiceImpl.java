package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dto.CrudOperationResponseDto;
import com.hftamayo.java.todo.dto.pagination.CursorPaginationDto;
import com.hftamayo.java.todo.dto.pagination.PageRequestDto;
import com.hftamayo.java.todo.dto.pagination.PaginatedDataDto;
import com.hftamayo.java.todo.dto.roles.RolesResponseDto;
import com.hftamayo.java.todo.entity.Roles;
import com.hftamayo.java.todo.mapper.TaskMapper;
import com.hftamayo.java.todo.repository.TaskRepository;
import com.hftamayo.java.todo.dto.task.TaskResponseDto;
import com.hftamayo.java.todo.entity.Task;
import com.hftamayo.java.todo.services.TaskService;
import com.hftamayo.java.todo.utilities.PaginationUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.hftamayo.java.todo.exceptions.ResourceNotFoundException;
import com.hftamayo.java.todo.exceptions.DuplicateResourceException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    //helper methods:
    private Optional<Task> getTaskById(long taskId) {
        return taskRepository.findTaskById(taskId);
    }

    private Optional<Task> getTaskByTitle(String taskTitle) {
        return taskRepository.findTaskByTitle(taskTitle);
    }

    @NotNull
    private CrudOperationResponseDto<TaskResponseDto> searchTaskByCriteria(Specification<Task> specification) {
        List<Task> taskList = taskRepository.findAll(specification);
        if (!taskList.isEmpty()) {
            List<TaskResponseDto> tasksDtoList = taskList.stream().map(taskMapper::taskToDto).toList();
            return new CrudOperationResponseDto(200, "OPERATION SUCCESSFUL", tasksDtoList);
        } else {
            return new CrudOperationResponseDto(404, "NO TASKS FOUND");
        }
    }

    private static @NotNull Task getExistingTask(Task updatedTask, Optional<Task> requestedTaskOptional) {
        Task existingTask = requestedTaskOptional.get();

        if (updatedTask.getTitle() != null) {
            existingTask.setTitle(updatedTask.getTitle());
        }
        if (updatedTask.getDescription() != null) {
            existingTask.setDescription(updatedTask.getDescription());
        }
        if (updatedTask.isStatus() != existingTask.isStatus()) {
            existingTask.setStatus(updatedTask.isStatus());
        }
        return existingTask;
    }

    //persistence methods
    @Override
    public CrudOperationResponseDto<TaskResponseDto> getTasks() {
        List<Task> taskList = taskRepository.findAll();
        if (!taskList.isEmpty()) {
            List<TaskResponseDto> tasksDtoList = taskList.stream().map(taskMapper::taskToDto).toList();
            return new CrudOperationResponseDto(200, "OPERATION SUCCESSFUL", tasksDtoList);
        } else {
            throw new ResourceNotFoundException("Task", "all");
        }
    }

    @Override
    public CrudOperationResponseDto<TaskResponseDto> getTask(long taskId) {
        Optional<Task> taskOptional = getTaskById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            TaskResponseDto taskToDto = taskMapper.taskToDto(task);
            return new CrudOperationResponseDto(200, "OPERATION SUCCESSFUL", taskToDto);
        } else {
            throw new ResourceNotFoundException("Task", taskId);
        }
    }

    @Override
    public CrudOperationResponseDto<TaskResponseDto> getTaskByCriteria(String criteria, String value) {
        Specification<Task> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(criteria), value);
        return searchTaskByCriteria(specification);
    }

    @Override
    public CrudOperationResponseDto<TaskResponseDto> getTaskByCriterias(String criteria, String value,
                                                                        String criteria2, String value2) {
        Specification<Task> specification = (root, query, criteriaBuilder)
                -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get(criteria), value),
                criteriaBuilder.equal(root.get(criteria2), value2)
        );
        return searchTaskByCriteria(specification);
    }

    @Override
    public PaginatedDataDto<TaskResponseDto> getPaginatedTasks(PageRequestDto pageRequestDto) {
        Pageable pageable = PageRequest.of(
                pageRequestDto.getPage(),
                pageRequestDto.getSize()
        );
        Page<Task> taskPage = taskRepository.findAll(pageable);
        List<TaskResponseDto> content = taskPage.getContent().stream()
                .map(taskMapper::toTaskResponseDto)
                .toList();

        CursorPaginationDto pagination = PaginationUtils.toCursorPagination(taskPage, pageRequestDto.getSort());

        return new PaginatedDataDto<>(content, pagination);
    }

    @Transactional
    @Override
    public CrudOperationResponseDto<TaskResponseDto> saveTask(Task newTask) {
        Optional<Task> requestedTask = getTaskByTitle(newTask.getTitle());
        if (!requestedTask.isPresent()) {
            Task savedTask = taskRepository.save(newTask);
            TaskResponseDto taskToDto = taskMapper.taskToDto(savedTask);
            return new CrudOperationResponseDto(201, "OPERATION SUCCESSFUL", taskToDto);
        } else {
            throw DuplicateResourceException.withFieldValue("title", newTask.getTitle());
        }
    }

    @Transactional
    @Override
    public CrudOperationResponseDto<TaskResponseDto> updateTask(long taskId, Task updatedTask) {
        Optional<Task> requestedTaskOptional = getTaskById(taskId);
        if (requestedTaskOptional.isPresent()) {
            Task existingTask = getExistingTask(updatedTask, requestedTaskOptional);
            Task savedTask = taskRepository.save(existingTask);
            TaskResponseDto taskToDto = taskMapper.taskToDto(savedTask);
            return new CrudOperationResponseDto(200, "OPERATION SUCCESSFUL", taskToDto);
        } else {
            throw new ResourceNotFoundException("Task", taskId);
        }
    }

    @Transactional
    @Override
    public CrudOperationResponseDto deleteTask(long taskId) {
        Optional<Task> requestedTask = getTaskById(taskId);
        if (requestedTask.isPresent()) {
            taskRepository.deleteTaskById(requestedTask.get().getId());
            return new CrudOperationResponseDto(200, "OPERATION SUCCESSFUL");
        } else {
            throw new ResourceNotFoundException("Task", taskId);
        }
    }

}







