package com.hftamayo.java.todo.Controller;

import com.hftamayo.java.todo.Model.Task;
import com.hftamayo.java.todo.Repository.TodoRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TodoController {
    @GetMapping(value = "/saludo")
    public String holaMundo(){
        return "Hola Mundo";
    }

    @GetMapping(value = "/tasks")
    public List<Task> getTasks(){
        return TodoRepository.findAll();
    }
}
