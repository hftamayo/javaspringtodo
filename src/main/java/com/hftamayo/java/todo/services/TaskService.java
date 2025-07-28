package com.hftamayo.java.todo.services;

import com.hftamayo.java.todo.dto.pagination.PageRequestDto;
import com.hftamayo.java.todo.dto.pagination.PaginatedDataDto;
import com.hftamayo.java.todo.entity.Task;
import com.hftamayo.java.todo.dto.task.TaskResponseDto;

import java.util.List;

public interface TaskService {
    List<TaskResponseDto> getTasks();
    TaskResponseDto getTask(long taskId);
    TaskResponseDto getTaskByCriteria(String criteria, String value);
    TaskResponseDto getTaskByCriterias(String criteria, String value, String criteria2, String value2);
    PaginatedDataDto<TaskResponseDto> getPaginatedTasks(PageRequestDto pageRequestDto);

    TaskResponseDto saveTask(Task newTask);
    TaskResponseDto updateTask(long taskId, Task updatedTask);
    void deleteTask(long taskId);
}

