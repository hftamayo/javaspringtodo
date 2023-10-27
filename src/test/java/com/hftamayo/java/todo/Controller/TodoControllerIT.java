package com.hftamayo.java.todo.Controller;

import com.hftamayo.java.todo.Model.Task;
import com.hftamayo.java.todo.Services.TodoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerIT {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoService todoService;

    @Test
    public void testGetTasksEndpoint() throws Exception {
        Task task = new Task(1L, "Task 1", "Description 1", LocalDateTime.now(), LocalDateTime.now(), true);
        todoService.saveTask(task);

        mockMvc.perform(MockMvcRequestBuilders.get("/todos/alltasks"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Task 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Description 1"));
    }

    @Test
    public void TestGetTaskByIdEndpoint() throws Exception {
        Task task = new Task(1L, "Task 1", "Description 1", LocalDateTime.now(), LocalDateTime.now(), true);
        todoService.saveTask(task);

        mockMvc.perform(MockMvcRequestBuilders.get("/todos/gettaskbyid/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Task 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description 1"));
    }
}
