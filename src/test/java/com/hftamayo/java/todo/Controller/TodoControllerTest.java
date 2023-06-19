package com.hftamayo.java.todo.Controller;

import com.hftamayo.java.todo.Model.Task;
import com.hftamayo.java.todo.Services.TodoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest({TodoController.class})
public class TodoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    TodoService todoService;

    @Test
    @DisplayName("saving a task")
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




}
