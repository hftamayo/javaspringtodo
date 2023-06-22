package com.hftamayo.java.todo.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hftamayo.java.todo.Model.Task;
import com.hftamayo.java.todo.Services.TodoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.CoreMatchers.is;

@RunWith(SpringRunner.class)
@WebMvcTest({TodoController.class})
public class TodoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    TodoService todoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("get all available task")
    public void givenUserRequest_whenGetTasks_thenReturnRecords() throws Exception {
        List<Task> tasksList = Arrays.asList(
                new Task("go to the supermarket", "cheese, fruits, veggies"),
                new Task("call the doctor", "make appointment"),
                new Task("pay bills", "telephone, cable, water")
        );

        when(todoService.getTasks()).thenReturn(tasksList);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/tasks"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(tasksList.size())));
    }

    @Test
    @DisplayName("saving a task successfully")
    public void givenValidValues_whenNewTask_thenSaveTaskSuccessfully() throws Exception {
        Task newTask = Task.builder()
                .title("pay salaries")
                .description("don't be a stupid and pay salaries to the employees")
                .build();
        given(todoService.newTask(any(Task.class))).willAnswer((invocation)->invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/savetask")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTask)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is(newTask.getTitle())))
                .andExpect(jsonPath("$.description", is(newTask.getDescription())));

    }


    @Test
    @DisplayName("delete an existing task")
    public void givenTaskId_whenDeleteTask_thenReturnSuccess() throws Exception {
        long taskId = 1L;
        willDoNothing().given(todoService).deleteTask(taskId);

        ResultActions response = mockMvc.perform(delete("/deletetask/{taskId}", taskId));

        response.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("update a task successfully")
    public void givenTaskId_whenUpdateTask_thenReturnSuccess() throws Exception {
        long taskId = 1L;
        Task savedTask = Task.builder()
                .title("study history")
                .description("History of Martin Luther King")
                .build();

        Task updatedTask = Task.builder()
                .title("buy bread")
                .description("don't forget the change")
                .build();

        given(todoService.getTaskById(taskId)).willReturn(Optional.of(savedTask));
        given(todoService.updateTask(any(Task.class)))
        .willAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(put("/updatetask/{taskId}", taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTask)));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.title", is(updatedTask.getTitle())))
                .andExpect(jsonPath("$.description", is(updatedTask.getDescription())));

    }

    @Test
    @DisplayName("update a task failing")
    public void givenTaskId_whenUpdateTask_thenReturnFailed() throws Exception {
        long taskId = 1L;
        Task savedTask = Task.builder()
                .title("study history")
                .description("History of Matin Luther King")
                .build();

        Task updatedTask = Task.builder()
                .title("buy bread")
                .description("don't forget the change")
                .build();

        given(todoService.getTaskById(taskId)).willReturn(Optional.empty());
        given(todoService.updateTask(any(Task.class)));
        .willAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(put("/updatetask/{taskId}", taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTask)));

        response.andExpect(status().isNotFound())
                .andDo(print());
    }

}
