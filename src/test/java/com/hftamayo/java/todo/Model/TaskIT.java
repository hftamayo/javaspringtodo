package com.hftamayo.java.todo.Model;

import com.hftamayo.java.todo.Repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TaskIT {
    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void testTaskSaveAndRetrieve() {
        Task task = new Task();
        task.setTitle("Sample Task 1");
        task.setDescription("Task Description 1");
        task.setStatus(true);

        todoRepository.save(task);
        Task retrievedTask = todoRepository.findByTitle("Task Description 1");

        assertNotNull(retrievedTask);
        assertEquals("Sample Task 1", retrievedTask.getTitle());
        assertEquals("Task Description 1", retrievedTask.getDescription());
        assertTrue(retrievedTask.isStatus());
        assertNotNull(retrievedTask.getDateAdded());
        assertNotNull(retrievedTask.getDateUpdated());
        assertNotNull(retrievedTask.getId());
    }

    @Test
    public void testTaskUpdate() {
        Task task = new Task();
        task.setTitle("Sample Task 1");
        task.setDescription("Task Description 1");
        task.setStatus(true);
        todoRepository.save(task);

        Task retrievedTask = todoRepository.findByTitle("Sample Task 1");
        assertNotNull(retrievedTask);
        retrievedTask.setDescription("Updated Description 1");
        todoRepository.save(retrievedTask);
        Task updatedTask = todoRepository.findByTitle("Sample Task 1");

        assertNotNull(updatedTask);
        assertEquals("Updated Description 1", updatedTask.getDescription());
        assertNotNull(updatedTask.getDateUpdated());
        assertEquals(retrievedTask.getDateAdded(), updatedTask.getDateAdded());
        assertEquals(retrievedTask.getId(), updatedTask.getId());
    }
}
