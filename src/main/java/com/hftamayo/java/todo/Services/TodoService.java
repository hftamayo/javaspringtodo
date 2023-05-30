package com.hftamayo.java.todo.Services;

import com.hftamayo.java.todo.Model.Task;
import com.hftamayo.java.todo.Repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Task deletedTask = todoRepository.findById(id).get();
        todoRepository.delete(deletedTask);
    }
}
