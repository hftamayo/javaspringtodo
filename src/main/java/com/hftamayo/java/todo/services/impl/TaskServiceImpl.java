package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dao.TaskDao;
import com.hftamayo.java.todo.exceptions.EntityAlreadyExistsException;
import com.hftamayo.java.todo.model.Task;
import com.hftamayo.java.todo.services.TaskService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    @Autowired
    private final TaskDao taskDao;

    public List<Task> getTasks(){
        return taskDao.getTasks();
    }

    public Optional<Task> getTaskById(long taskId){
        return taskDao.getTaskById(taskId);
    }

    public List<Task> getAllTasksByStatus(boolean taskStatus){
        return taskDao.getTasksByStatus(taskStatus);
    }

    public Optional<Task> getTaskByTitle(String taskTitle){
        return taskDao.getTaskByTitle(taskTitle);
    }

    public long countAllTaskByStatus(boolean taskStatus){
        return taskRepository.countAllByStatus(taskStatus);
    }

    @Transactional
    @Override
    public Task saveTask(Task task){
        Optional<Task> requestedTask = getTaskByTitle(task.getTitle());
        if(requestedTask.isPresent()){
            throw new EntityAlreadyExistsException("Title already exists");
        }
        return taskDao.saveTask(task);
    }

    @Transactional
    @Override
    public Task updateTask(long taskId, Task task) {
        Optional<Task> requestedTask = getTaskById(taskId);
        if(requestedTask.isPresent()){
            Task updateTask = requestedTask.get();
            updateTask.setTitle(task.getTitle());
            updateTask.setDescription(task.getDescription());
            updateTask.setStatus(task.isStatus());
            return taskDao.updateTask(taskId, updateTask);
        } else {
            throw new EntityNotFoundException("Task does not exist");
        }
    }

    @Transactional
    @Override
    public void deleteTask(long taskId) {
        Optional<Task> requestedTask = getTaskById(taskId);
        if(requestedTask.isPresent()){
            taskDao.deleteTask(requestedTask.get().getId());
        } else {
            throw new EntityNotFoundException("Task does not exist");
        }
    }
    
}







