package com.hftamayo.java.todo.Controller;

import com.hftamayo.java.todo.Model.Task;
import com.hftamayo.java.todo.Services.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TodoController {
    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService){
        this.todoService = todoService;
    }


    @GetMapping(value = "/todos/alltasks")
    @ResponseStatus(HttpStatus.OK)
    public List<Task> getTasks(){
        return todoService.getTasks();
    }

    @GetMapping(value = "/todos/gettask/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public Task getTask(@PathVariable long taskId){
        return todoService.getTaskById(taskId);
    }

    @GetMapping(value = "/todos/gettask/{taskTitle}")
    @ResponseStatus(HttpStatus.OK)
    public Task getTaskByTitle(@PathVariable String taskTitle){
        return todoService.getTaskByTitle(taskTitle);
    }



    @PostMapping(value = "/todos/savetask")
    @ResponseStatus(HttpStatus.CREATED)
    public Task saveTask(@RequestBody Task task){
        return todoService.saveTask(task);
    }

    @PutMapping(value="/todos/updatetask/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable long taskId, @RequestBody Task task){
        Task updatedTask = todoService.updateTask(taskId, task);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping(value="/todos/deletetask/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteTask(@PathVariable long taskId){
        todoService.deleteTask(taskId);
        return "data deleted";
    }
}
