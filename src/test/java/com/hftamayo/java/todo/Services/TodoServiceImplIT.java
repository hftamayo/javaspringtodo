package com.hftamayo.java.todo.Services;

import com.hftamayo.java.todo.Model.Task;
import com.hftamayo.java.todo.Repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class TodoServiceImplIT {
    @Autowired
    private TodoService todoService;

    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void testGetTasks(){
        Task task1 = new Task(1L, "Task 1", "Description 1", LocalDateTime.now(), LocalDateTime.now(), true);
        Task task2 = new Task(2L, "Task 2", "Description 2", LocalDateTime.now(), LocalDateTime.now(), true);

        todoRepository.save(task1);
        todoRepository.save(task2);

        List<Task> tasks = todoService.getTasks();
        assertEquals(2, tasks.size());
        assertEquals("Task 1", tasks.get(0).getTitle());
        assertEquals("Task 2", tasks.get(1).getTitle());
    }
}
