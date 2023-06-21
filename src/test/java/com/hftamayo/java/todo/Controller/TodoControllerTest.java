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
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    @DisplayName("saving a task successfully")
    public void givenValidValues_whenNewTask_thenSaveTask() throws Exception {
        Task newTask = new Task("go to the supermarket", "cheese, fruits, veggies");
        when(todoService.newTask(any())).thenReturn(Boolean.TRUE);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/savetask")
                        .content(asJsonString(newTask))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().string("Task saved"));
    }

    @Test
    @DisplayName("saving a task failed process")
    public void givenValidValues_whenNewTask_thenSaveTaskFailing() throws Exception {
        Task newTask = new Task("go to the supermarket", "cheese, fruits, veggies");
        when(todoService.newTask(any())).thenReturn(Boolean.FALSE);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/savetask")
                        .content(asJsonString(newTask))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().string("Task could not be saved"));
    }

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
                .andExpect(content().json("[{}, {}, {}]"));
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
    @DisplayName("update a task")
    public void givenTaskId_whenUpdateTask_thenReturnSuccess() throws Exception {
        long taskId = 1L;
        Task savedTask = Task.builder()
                .title("study history")
                .description("History of Matin Luther King")
                .build();

        Task updatedTask = Task.builder()
                .title("buy bread")
                .description("don't forget the change")
                .build();

        given(todoService.getTaskById(taskId)).willReturn(Optional.of(savedTask));
        given(todoService.updateTask(any(Task.class)));
        .willAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(put("/updatetask/{taskId}", taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTask)));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.title", is(updatedTask.getTitle())))
                .andExpect(jsonPath("$.description", is(updatedTask.getDescription())));

    }



    public static String asJsonString(final Object obj){
        try{
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
