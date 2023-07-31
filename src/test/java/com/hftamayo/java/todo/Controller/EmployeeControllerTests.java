package com.hftamayo.java.todo.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hftamayo.java.todo.Model.Task;
import com.hftamayo.java.todo.Services.TodoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.*;

@WebMvcTest

public class EmployeeControllerTests {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("get all available Tasks")
    public void givenListOfTasks_whenGetAllTasks_thenReturnTasksList() throws Exception {

        List<Task> listOfTasks = new ArrayList<>();
        listOfTasks.add(Task.builder()
                .title("listen YT Playlist")
                .description("R & B PlayList")
                .dateAdded(LocalDateTime.now())
                .dateUpdated(LocalDateTime.now())
                .status(true)
                .build());
        listOfTasks.add(Task.builder()
                .title("listen Spotify Playlist")
                .description("New Age PlayList")
                .dateAdded(LocalDateTime.now())
                .dateUpdated(LocalDateTime.now())
                .status(true)
                .build());
        given(todoService.getTasks()).willReturn(listOfTasks);

        ResultActions response = mockMvc.perform(get("/tasks"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfTasks.size())));

    }

    @Test
    @DisplayName("insert a non-existent task")
    public void givenTaskObject_whenCreateTask_thenReturnSavedTask() throws Exception{
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
        Task task = Task.builder()
                .title("Go to the Medician")
                .description("keep working on your health")
                .dateAdded(LocalDateTime.of(2023, 07, 01, 12, 0))
                .dateUpdated(LocalDateTime.of(2023, 07, 01, 12, 0))
                .status(true)
                .build();
        given(todoService.saveTask(any(Task.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/savetask")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)));

        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is(task.getTitle())))
                .andExpect(jsonPath("$.description", is(task.getDescription())))
                .andExpect(jsonPath("$.dateAdded", is(task.getDateAdded())))
                .andExpect(jsonPath("$.dateUpdated", is(task.getDateUpdated())))
                .andExpect(jsonPath("$.status", is(task.isStatus())))
        ;
    }


//    @Test
//    @DisplayName("get a task by id")
//    public void givenTaskId_whenGetTaskById_thenRetunTaskObject() throws Exception{
//
//        long taskId = 1L;
//        Task task = Task.builder()
//                .title("listen Cybersec podcast")
//                .description("Hackermacker or TCM")
//                .build();
//        given(todoService.getTaskById(taskId)).thenReturn(Optional.of(task));
//
//        ResultActions response = mockMvc.perform(get("/task/{id}", taskId));
//
//        response.andExpect(status().isOk())
//                .andDo(print())
//                .andExpect(jsonPath("$.title", is(task.getTitle())))
//                .andExpect(jsonPath("$.description", is(task.getDescription())));
//    }

}
