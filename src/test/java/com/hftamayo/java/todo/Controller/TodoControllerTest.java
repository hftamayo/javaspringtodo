package com.hftamayo.java.todo.Controller;

import com.hftamayo.java.todo.Services.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest({TodoController.class})
public class TodoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    TodoService todoService;

}
