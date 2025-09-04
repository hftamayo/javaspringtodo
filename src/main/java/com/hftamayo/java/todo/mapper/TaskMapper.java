package com.hftamayo.java.todo.mapper;

import com.hftamayo.java.todo.dto.task.TaskResponseDto;
import com.hftamayo.java.todo.entity.Task;
import com.hftamayo.java.todo.entity.User;
import com.hftamayo.java.todo.repository.UserRepository;
import org.springframework.stereotype.Component;
import com.hftamayo.java.todo.exceptions.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskMapper {
    private final UserRepository userRepository;

    public TaskMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public TaskResponseDto toTaskResponseDto(Task task) {
        return new TaskResponseDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isStatus(),
                task.getDateAdded().toString(),
                task.getUser().getEmail()
        );
    }

    public Task toEntity(TaskResponseDto taskResponseDto) {
        Task task = new Task();
        task.setId(taskResponseDto.getId());
        task.setTitle(taskResponseDto.getTitle());
        task.setDescription(taskResponseDto.getDescription());
        task.setStatus(taskResponseDto.isStatus());
        task.setDateAdded(LocalDateTime.parse(taskResponseDto.getDateAdded()));

        User owner = getOwnerByEmail(taskResponseDto.getOwner());
        task.setUser(owner);
        return task;
    }

    private User getOwnerByEmail(String owner) {
        return userRepository.findUserByEmail(owner)
                .orElseThrow(() -> new ResourceNotFoundException("User", owner));
    }

    public TaskResponseDto taskToDto(Task task) {
        return toTaskResponseDto(task);
    }

    public List<TaskResponseDto> taskListToDto(List<Task> tasks) {
        return tasks.stream()
                .map(this::toTaskResponseDto)
                .collect(Collectors.toList());
    }
}
