package com.hftamayo.java.todo.Controller;

import com.hftamayo.java.todo.Model.Task;
import com.hftamayo.java.todo.Services.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.OK)
    public List<Task> getTasks(){
        return todoService.getTasks();
    }

    @PostMapping(value = "/savetask")
    @ResponseStatus(HttpStatus.CREATED)
    public Task saveTask(@RequestBody Task task){
        return todoService.saveTask(task);
    }

    @PutMapping(value="/updatetask/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public Task updateTask(@PathVariable long taskId, @RequestBody Task task){
        return todoService.updateTask(taskId, task);
    }

    @DeleteMapping(value="/deletetask/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteTask(@PathVariable long taskId){
        todoService.deleteTask(taskId);
        return "data deleted";
    }
}
