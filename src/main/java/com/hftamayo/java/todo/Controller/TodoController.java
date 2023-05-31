package com.hftamayo.java.todo.Controller;

import com.hftamayo.java.todo.Model.Task;
import com.hftamayo.java.todo.Repository.TodoRepository;
import com.hftamayo.java.todo.Services.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TodoController {
    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService){
        this.todoService = todoService;
    }


    @GetMapping(value = "/tasks")
    public List<Task> getTasks(){
        return todoService.getTasks();
    }

    @PostMapping(value = "/savetask")
    public String saveTask(@RequestBody Task task){
        todoService.newTask(task);
        return "Task saved";
    }

    @PutMapping(value="/updatetask/{id}")
    public String updateTask(@PathVariable long id, @RequestBody Task task){
        todoService.updateTask(id, task);
        return "data updated";
    }

    @DeleteMapping(value="/deletetask/{id}")
    public String deleteTask(@PathVariable long id){
        todoService.deleteTask(id);
        return "data deleted";
    }
}
