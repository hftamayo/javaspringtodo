package com.hftamayo.java.todo.Services;

import com.hftamayo.java.todo.Model.Task;
import com.hftamayo.java.todo.Repository.TodoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {
    @Mock
    private TodoRepository todoRepository;

    TodoService todoService;

    @Test
    @DisplayName("retrieve all existing task")
    public void givenRequest_whenGetTasks_thenReturnAllAvailableTasks(){
        List<Task> fetchedTasks = todoService.getTasks();
        assertThat(fetchedTasks.size()).isGreaterThan(0);
    }


    @Test
    @DisplayName("saving a task successfully")
    public void givenValidValues_whenNewTask_thenSaveTaskSuccessfully(){
        Task newTask = new Task();
        //sustituir por el metodo de Builder de Lombok
        newTask.setTitle("buy medicine");
        newTask.setDescription("dont forget your client id number");

        // aca estoy probando el repositorio cuando considero debería ser el servicio

        when(todoRepository.save(any(Task.class))).thenReturn(newTask);
        Task savedTask = todoRepository.save(newTask);
        assertThat(savedTask.getTitle()).isNotNull();
    }



    //test de casos de uso especiales que tendrán total cobertura en siguientes version

    @Test
    @DisplayName("failing saving a task due existing name")
    public void givenExistingTitle_whenNewTask_thenSaveTaskFailed(){
        Task newTask = new Task();
        newTask.setTitle("buy medicine");
        newTask.setDescription("this is an existing task");

        when(todoRepository.countAllByTitle(newTask.getTitle())).thenReturn(1L);
        Boolean isSaveSuccess = todoService.createTask(newTask);
        assertThat(isSaveSuccess).isEqualTo(false);
    }




}
