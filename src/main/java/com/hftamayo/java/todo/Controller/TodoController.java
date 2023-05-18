package com.hftamayo.java.todo.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TodoController {
    @GetMapping(value = "/saludo")
    public String holaMundo(){
        return "Hola Mundo";
    }
}
