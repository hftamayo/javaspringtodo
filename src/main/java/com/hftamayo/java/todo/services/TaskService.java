package com.hftamayo.java.todo.services;

import com.hftamayo.java.todo.dto.CrudOperationResponseDto;
import com.hftamayo.java.todo.dto.pagination.PageRequestDto;
import com.hftamayo.java.todo.dto.pagination.PaginatedDataDto;
import com.hftamayo.java.todo.entity.Task;
import com.hftamayo.java.todo.dto.task.TaskResponseDto;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    CrudOperationResponseDto<TaskResponseDto> getTasks();
    CrudOperationResponseDto<TaskResponseDto> getTask(long taskId);
    CrudOperationResponseDto<TaskResponseDto> getTaskByCriteria(String criteria, String value);
    CrudOperationResponseDto<TaskResponseDto> getTaskByCriterias(String criteria, String value, String criteria2, String value2);
    PaginatedDataDto<TaskResponseDto> getPaginatedTasks(PageRequestDto pageRequestDto);

    CrudOperationResponseDto<TaskResponseDto> saveTask(Task newTask);
    CrudOperationResponseDto<TaskResponseDto> updateTask(long taskId, Task updatedTask);
    CrudOperationResponseDto deleteTask(long taskId);

}

