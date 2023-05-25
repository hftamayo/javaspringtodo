package com.hftamayo.java.todo.Services;

import com.hftamayo.java.todo.Model.Task;
import com.hftamayo.java.todo.Repository.TodoRepository;

import java.util.List;

public class TodoService {

    private TodoRepository todoRepository;

    public List<Task> getTasks(){
        return todoRepository.findAll();
    }
}
