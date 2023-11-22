package com.hftamayo.java.todo.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    public void deleteAllRecords() {
        todoRepository.deleteAll();
    }

    @Test
    public void testGetTasks() throws Exception {
        Task task = new Task(1L, "Task 1", "Description 1", LocalDateTime.now(), LocalDateTime.now(), true);
        todoService.saveTask(task);

        mockMvc.perform(MockMvcRequestBuilders.get("/todos/alltasks"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Task 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Description 1"));
    }

    @Test
    public void TestGetTaskById() throws Exception {
        Task task = new Task(1L, "Task 1", "Description 1", LocalDateTime.now(), LocalDateTime.now(), true);
        Task savedTask = todoService.saveTask(task);

        mockMvc.perform(MockMvcRequestBuilders.get("/todos/gettaskbyid/" + savedTask.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Task 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description 1"));
    }

    @Test
    public void TestGetTaskByTitle() throws Exception {
        Task task = new Task(1L, "Task 1", "Description 1", LocalDateTime.now(), LocalDateTime.now(), true);
        Task savedTask = todoService.saveTask(task);

        mockMvc.perform(MockMvcRequestBuilders.get("/todos/gettaskbytitle/" + savedTask.getTitle()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Task 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description 1"));
    }

    @Test
    public void TestGetTasksByStatus() throws Exception {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1L, "Task 1", "Description 1", LocalDateTime.now(), LocalDateTime.now(), true));
        tasks.add(new Task(2L, "Task 2", "Description 2", LocalDateTime.now(), LocalDateTime.now(), false));
        tasks.add(new Task(3L, "Task 3", "Description 3", LocalDateTime.now(), LocalDateTime.now(), true));
        tasks.add(new Task(4L, "Task 4", "Description 4", LocalDateTime.now(), LocalDateTime.now(), true));
        tasks.forEach(todoService::saveTask);

        mockMvc.perform(MockMvcRequestBuilders.get("/todos/gettasksbystatus/true"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Task 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Task 3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].title").value("Task 4"));
    }

    @Test
    public void TestCountTasksByStatus() throws Exception {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1L, "Task 1", "Description 1", LocalDateTime.now(), LocalDateTime.now(), true));
        tasks.add(new Task(2L, "Task 2", "Description 2", LocalDateTime.now(), LocalDateTime.now(), false));
        tasks.add(new Task(3L, "Task 3", "Description 3", LocalDateTime.now(), LocalDateTime.now(), true));
        tasks.add(new Task(4L, "Task 4", "Description 4", LocalDateTime.now(), LocalDateTime.now(), true));
        tasks.forEach(todoService::saveTask);

        //status true

        mockMvc.perform(MockMvcRequestBuilders.get("/todos/counttasksbystatus/true"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(3L));

        //status false
        mockMvc.perform(MockMvcRequestBuilders.get("/todos/counttasksbystatus/false"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(1L));
    }

    @Test
    public void testSaveTask() throws Exception {
        Task newTask = new Task(1L, "New Task", "Description", LocalDateTime.now(), LocalDateTime.now(), true);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc.perform(MockMvcRequestBuilders.post("/todos/savetask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTask)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("New Task"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(true));
    }

    @Test
    public void testDeleteTask() throws Exception {
        Task newTask = new Task(1L, "New Task", "Description", LocalDateTime.now(), LocalDateTime.now(), true);
        todoService.saveTask(newTask);

        mockMvc.perform(MockMvcRequestBuilders.delete("/todos/deletetask/{taskId}", newTask.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("data deleted"));
    }

    @Test
    public void testUpdateTask() throws Exception {
        Task newTask = new Task(1L, "New Task", "Description", LocalDateTime.now(), LocalDateTime.now(), true);
        Task savedTask = todoService.saveTask(newTask);

        savedTask.setTitle("Updated Task");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc.perform(MockMvcRequestBuilders.put("/todos/updatetask/" + savedTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedTask)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Updated Task"));
    }
}
