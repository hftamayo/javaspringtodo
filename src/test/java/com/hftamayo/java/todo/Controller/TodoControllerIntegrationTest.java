package com.hftamayo.java.todo.Controller;

import com.hftamayo.java.todo.Model.Task;
import com.hftamayo.java.todo.Repository.TodoRepository;
import com.hftamayo.java.todo.Services.TodoService;
import com.hftamayo.java.todo.TodoApplication;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {TodoApplication.class})
@AutoConfigureMockMvc
public class TodoControllerIntegrationTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoService todoService;

    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    public void deleteAllRecords(){
        todoRepository.deleteAll();
    }

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
        Task savedTask = todoService.saveTask(task);

        mockMvc.perform(MockMvcRequestBuilders.get("/todos/gettaskbyid/" + savedTask.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Task 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description 1"));
    }
}
