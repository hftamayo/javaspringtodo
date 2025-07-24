package com.hftamayo.java.todo.services;

import com.hftamayo.java.todo.dto.EndpointResponseDto;
import com.hftamayo.java.todo.dto.pagination.PageRequestDto;
import com.hftamayo.java.todo.dto.pagination.PaginatedDataDto;
import com.hftamayo.java.todo.entity.Task;
import com.hftamayo.java.todo.dto.task.TaskResponseDto;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    EndpointResponseDto<TaskResponseDto> getTasks();
    EndpointResponseDto<TaskResponseDto> getTask(long taskId);
    EndpointResponseDto<TaskResponseDto> getTaskByCriteria(String criteria, String value);
    EndpointResponseDto<TaskResponseDto> getTaskByCriterias(String criteria, String value, String criteria2, String value2);
    PaginatedDataDto<TaskResponseDto> getPaginatedTasks(PageRequestDto pageRequestDto);

    EndpointResponseDto<TaskResponseDto> saveTask(Task newTask);
    EndpointResponseDto<TaskResponseDto> updateTask(long taskId, Task updatedTask);
    EndpointResponseDto deleteTask(long taskId);

}

