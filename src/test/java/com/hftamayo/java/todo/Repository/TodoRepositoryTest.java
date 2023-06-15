package com.hftamayo.java.todo.Repository;

import com.hftamayo.java.todo.Model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

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
}
