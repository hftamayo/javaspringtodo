package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.exceptions.EntityAlreadyExistsException;
import com.hftamayo.java.todo.model.Task;
import com.hftamayo.java.todo.repository.TaskRepository;
import com.hftamayo.java.todo.services.TaskService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    public List<Task> getTasks(){
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(long taskId){
        return taskRepository.findById(taskId);
    }

    public List<Task> getAllTasksByStatus(boolean taskStatus){
        return taskRepository.findAllByStatus(taskStatus);
    }

    public long countAllTaskByStatus(boolean taskStatus){
        return taskRepository.countAllByStatus(taskStatus);
    }

    public Optional<Task> getTaskByTitle(String taskTitle){
        return taskRepository.findByTitle(taskTitle);
    }

    @Transactional
    @Override
    public Task saveTask(Task task){
        Optional<Task> requestedTask = taskRepository.findByTitle(task.getTitle());
        if(requestedTask.isPresent()){
            throw new EntityAlreadyExistsException("Title already exists");
        }
        return taskRepository.save(task);
    }

    @Transactional
    @Override
    public Task updateTask(long taskId, Task task) {
        Optional<Task> requestedTask = getTaskById(taskId);
        if(requestedTask.isPresent()){
            Task updateTask = requestedTask.get();
            updateTask.setTitle(task.getTitle());
            updateTask.setDescription(task.getDescription());
            return taskRepository.save(updateTask);
        } else {
            throw new EntityNotFoundException("Task does not exist");
        }
    }

    @Transactional
    @Override
    public void deleteTask(long taskId) {
        Optional<Task> requestedTask = getTaskById(taskId);
        if(requestedTask.isPresent()){
            taskRepository.deleteById(requestedTask.get().getId());
        } else {
            throw new EntityNotFoundException("Task does not exist");
        }
    }
    
}







