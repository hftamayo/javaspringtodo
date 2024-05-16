package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.model.Task;
import com.hftamayo.java.todo.repository.TaskRepository;
import com.hftamayo.java.todo.services.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasks(){
        return taskRepository.findAll();
    }

    public List<Task> getAllTasksByStatus(boolean taskStatus){
        return taskRepository.findAllByStatus(taskStatus);
    }

    public long countAllTaskByStatus(boolean taskStatus){
        return taskRepository.countAllByStatus(taskStatus);
    }

    public Task getTaskById(long taskId){
        return taskRepository.findById(taskId).get();
        //return todoRepository.findById(taskId).orElseThrow(EntityNotFoundException::new);
    }

    public Task getTaskByTitle(String taskTitle){
        return taskRepository.findByTitle(taskTitle);
    }

    @Override
    public Task saveTask(Task task){
        Task requestedTask = taskRepository.findByTitle(task.getTitle());
        if(requestedTask != null && requestedTask.getId()>0){
            throw new RuntimeException("Title already exists");
        }
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(long taskId, Task task) {
        Task requestedTask = taskRepository.findById(taskId).get();
        if(requestedTask != null){
            requestedTask.setTitle(task.getTitle());
            requestedTask.setDescription(task.getDescription());
            return taskRepository.save(requestedTask);
        } else {
            throw new RuntimeException("Task does not exist");
        }
    }

    @Override
    public void deleteTask(long id) {
        boolean recordExists = this.taskRepository.existsById(id);
        if(recordExists){
            taskRepository.deleteById(id);
        } else {
            throw new RuntimeException("Task does not exist");
        }
    }
    
}







