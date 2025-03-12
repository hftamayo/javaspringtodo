package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dto.CrudOperationResponseDto;
import com.hftamayo.java.todo.mapper.TaskMapper;
import com.hftamayo.java.todo.repository.TaskRepository;
import com.hftamayo.java.todo.dto.task.TaskResponseDto;
import com.hftamayo.java.todo.exceptions.EntityAlreadyExistsException;
import com.hftamayo.java.todo.entity.Task;
import com.hftamayo.java.todo.services.TaskService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.empty;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public CrudOperationResponseDto<TaskResponseDto> getTasks() {
        try {
            List<Task> taskList = taskRepository.findAll();
            if (!taskList.isEmpty()) {
                List<TaskResponseDto> tasksDtoList = taskList.stream().map(taskMapper::taskToDto).toList();
                return new CrudOperationResponseDto(200, "OPERATION SUCCESSFUL", tasksDtoList);
            } else {
                return new CrudOperationResponseDto(404, "NO TASKS FOUND");
            }
        } catch (Exception e) {
            return new CrudOperationResponseDto(500, "INTERNAL SERVER ERROR");
        }
    }

    public Optional<Task> getTaskById(long taskId) {
        return taskRepository.findTaskById(taskId);
    }

    @Override
    public Optional<TaskResponseDto> getTask(long taskId) {
        Optional<Task> taskOptional = getTaskById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            TaskResponseDto dto = taskToDto(task);
            return Optional.of(dto);
        } else {
            return empty();
        }
    }

    @Override
    public Optional<Task> getTaskByTitle(String taskTitle) {
        return taskRepository.findTaskByTitle(taskTitle);
    }

    @Override
    public Optional<List<TaskResponseDto>> getTaskByCriteria(String criteria, String value) {
        Specification<Task> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(criteria), value);

        List<Task> taskList = taskRepository.findAll(specification);
        if (!taskList.isEmpty()) {
            return Optional.of(taskListToDto(taskList));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<TaskResponseDto>> getTaskByCriterias(String criteria, String value,
                                                              String criteria2, String value2) {
        Specification<Task> specification = (root, query, criteriaBuilder)
                -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get(criteria), value),
                criteriaBuilder.equal(root.get(criteria2), value2)
        );

        List<Task> taskList = taskRepository.findAll(specification);
        if (!taskList.isEmpty()) {
            return Optional.of(taskListToDto(taskList));
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public TaskResponseDto saveTask(Task newTask) {
        Optional<Task> requestedTask = getTaskByTitle(newTask.getTitle());
        if (!requestedTask.isPresent()) {
            Task savedTask = taskRepository.save(newTask);
            TaskResponseDto dto = taskToDto(savedTask);
            return dto;
        } else {
            throw new EntityAlreadyExistsException("Task title already exists: " +
                    requestedTask.get().getTitle());
        }
    }

    @Transactional
    @Override
    public TaskResponseDto updateTask(long taskId, Task updatedTask) {
        Optional<Task> requestedTaskOptional = getTaskById(taskId);
        if (requestedTaskOptional.isPresent()) {
            Task existingTask = requestedTaskOptional.get();
            existingTask.setTitle(updatedTask.getTitle());
            existingTask.setDescription(updatedTask.getDescription());
            existingTask.setStatus(updatedTask.isStatus());
            Task savedTask = taskRepository.save(existingTask);
            return taskToDto(savedTask);
        } else {
            throw new EntityNotFoundException("User not found");
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
            return new CrudOperationResponseDto(404, "TASK NOT FOUND");
        }
    }

    @Override
    public TaskResponseDto taskToDto(Task task) {
        String owner = task.getUser().getUsername();

        return new TaskResponseDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isStatus(),
                task.getDateAdded().toString(),
                owner
        );
    }

    private List<TaskResponseDto> taskListToDto(List<Task> tasks) {
        return tasks.stream().map(this::taskToDto).collect(Collectors.toList());
    }

}







