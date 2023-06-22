package com.hftamayo.java.todo.Services;

import com.hftamayo.java.todo.Model.Task;
import com.hftamayo.java.todo.Repository.TodoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {
    @Mock
    private TodoRepository todoRepository;

    Task task;

    @Test
    @DisplayName("saving a task successfully")
    public void givenValidValues_whenNewTask_thenSaveTaskSuccessfully(){
        Task newTask = new Task();
        newTask.setTitle("buy medicine");
        newTask.setDescription("dont forget your client id number");

        when(todoRepository.save(any(Task.class))).thenReturn(newTask);
        Task savedTask = todoRepository.save(newTask);
        assertThat(savedTask.getTitle()).isNotNull();
    }
}
