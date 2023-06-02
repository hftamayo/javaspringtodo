package com.hftamayo.java.todo.Services;

import com.hftamayo.java.todo.Model.Task;
import com.hftamayo.java.todo.Repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository){
        this.todoRepository = todoRepository;
    }

    public List<Task> getTasks(){
        return todoRepository.findAll();
    }

    public void newTask(Task task){
        Optional<Task> requestedTask = todoRepository.findTaskByTitle(task.getTitle());
        if(requestedTask.isPresent()){
            throw new IllegalStateException("Title already exists");
        }
        todoRepository.save(task);
    }

    public void deleteTask(long id) {
        boolean recordExists = this.todoRepository.existsById(id);
        if(recordExists){
            todoRepository.deleteById(id);
        }
    }

    public void updateTask(long id, Task task) {
        Task requestedTask = todoRepository.findById(id).get();
        requestedTask.setTitle(task.getTitle());
        requestedTask.setDescription(task.getDescription());

        todoRepository.save(requestedTask);
    }

//    public void ApiResponse updateTask(long id, Task task){
//        Task requestedTask = todoRepository.findById(id).get();
//        requestedTask.setTitle(task.getTitle());
//        requestedTask.setDescription(task.getDescription());
//        todoRepository.save(requestedTask);
//        return new ApiResponse(HttpStatus.OK.value), "task updated");
//    }
}
