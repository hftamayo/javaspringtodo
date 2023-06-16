package com.hftamayo.java.todo.Repository;

import com.hftamayo.java.todo.Model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DataJpaTest

public class TodoRepositoryTest {
    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    void initUseCase(){
        List<Task> tasks = Arrays.asList(
                new Task("supermarket list", "milk, cheese, fruits, veggies")
        );
        todoRepository.saveAll(tasks);
    }

    @AfterEach
    public void destroyAll(){
        todoRepository.deleteAll();
    }

    @Test
    void saveAll_success(){
        List<Task> tasks = Arrays.asList(
                new Task("pay the bills", "water, cable, electricity"),
                new Task("family appointment", "breakfast or lunch"),
                new Task("golang project", "keep working on it")
        );
        Iterable<Task> allTasks = todoRepository.saveAll(tasks);

        AtomicInteger validIdFound = new AtomicInteger();
        allTasks.forEach(task -> {
            if(task.getId()>0){
                validIdFound.getAndIncrement();
            }
        });
        assertThat(validIdFound.intValue()).isEqualTo(3);
    }
}
