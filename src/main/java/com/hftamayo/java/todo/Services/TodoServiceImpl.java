package com.hftamayo.java.todo.Services;

import com.hftamayo.java.todo.Model.Task;
import com.hftamayo.java.todo.Repository.TodoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoServiceImpl implements TodoService{
    private final TodoRepository todoRepository;

    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Task> getTasks(){
        return todoRepository.findAll();
    }

    public Task getTaskById(long taskId){
        return todoRepository.findById(taskId).orElseThrow(EntityNotFoundException::new);
    }

    public Task getTaskByTitle(String taskTitle){
        return todoRepository.findByTitle(taskTitle);
    }

    public List<Task> getAllTasksByStatus(boolean taskStatus){
        return todoRepository.findAllByStatus(taskStatus);
    }

    public long countAllTaskByStatus(boolean taskStatus){
        return todoRepository.countAllByStatus(taskStatus);
    }

    public Task saveTask(Task task){
        Task requestedTask = todoRepository.findByTitle(task.getTitle());
        if(requestedTask != null && requestedTask.getId()>0){
            throw new IllegalStateException("Title already exists");
        }
        return todoRepository.save(task);
    }

    public Task updateTask(Task task){
        return todoRepository.save(task);
    }

    // ejemplos a evaluar para metodos PUT y POST
//    public void updateTask(long id, Task task) {
//        Task requestedTask = todoRepository.findById(id).get();
//        requestedTask.setTitle(task.getTitle());
//        requestedTask.setDescription(task.getDescription());
//
//        todoRepository.save(requestedTask);
//    }

//    public void ApiResponse updateTask(long id, Task task){
//        Task requestedTask = todoRepository.findById(id).get();
//        requestedTask.setTitle(task.getTitle());
//        requestedTask.setDescription(task.getDescription());
//        todoRepository.save(requestedTask);
//        return new ApiResponse(HttpStatus.OK.value), "task updated");
//    }

    public void deleteTask(long id) {
        boolean recordExists = this.todoRepository.existsById(id);
        if(recordExists){
            todoRepository.deleteById(id);
        }
    }
    
}







