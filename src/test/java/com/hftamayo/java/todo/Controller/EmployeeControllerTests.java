package com.hftamayo.java.todo.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hftamayo.java.todo.Model.Task;
import com.hftamayo.java.todo.Services.TodoService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest

public class EmployeeControllerTests {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

//    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
//    @Autowired
//    private ObjectMapper objectMapper;

    @Test
    @DisplayName("get all available Tasks")
    public void givenListOfTasks_whenGetAllTasks_thenReturnTasksList() throws Exception {

        List<Task> listOfTasks = new ArrayList<>();
        listOfTasks.add(Task.builder().title("listen YT Playlist").description("R & B PlayList").build());
        listOfTasks.add(Task.builder().title("listen Spotify Playlist").description("New Age PlayList").build());
        given(todoService.getTasks()).willReturn(listOfTasks);

        ResultActions response = mockMvc.perform(get("/tasks"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfTasks.size())));

    }

    @Test
    @DisplayName("get a task by id")
    public void givenTaskId_whenGetTaskById_thenRetunTaskObject() throws Exception{

        long taskId = 1L;
        Task task = Task.builder()
                .title("listen Cybersec podcast")
                .description("Hackermacker or TCM")
                .build();
        given(todoService.getTaskById(taskId)).willReturn(Optional.of(task));

        ResultActions response = mockMvc.perform(get("/task/{id}", taskId));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.title", is(task.getTitle())))
                .andExpect(jsonPath("$.description", is(task.getDescription())));
    }




}
