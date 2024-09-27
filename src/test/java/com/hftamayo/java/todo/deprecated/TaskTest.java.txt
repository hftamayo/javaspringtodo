package com.hftamayo.java.todo.Model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

class TaskTest {

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.onCreate();
    }

    @Test
    @DisplayName("Class Instantiation")
    void testTaskInstantiation() {
        assertNotNull(task);
        assertEquals(0, task.getId());
        assertNull(task.getTitle());
        assertNull(task.getDescription());
        assertNotNull(task.getDateAdded());
        assertNotNull(task.getDateUpdated());
        assertTrue(task.isStatus());
    }

    @Test
    @DisplayName("Class setters and getters")
    void testGetterAndSetterMethods() {
        task.setId(1L);
        task.setTitle("Sample Task");
        task.setDescription("Task Description");
        task.setStatus(false);

        assertEquals(1L, task.getId());
        assertEquals("Sample Task", task.getTitle());
        assertEquals("Task Description", task.getDescription());
        assertFalse(task.isStatus());
    }

    @Test
    @DisplayName("getDaysAdded function")
    void testGetDaysAdded() {
        LocalDateTime dateAdded = LocalDateTime.now().minusDays(2);
        task.setDateAdded(dateAdded);

        long daysAdded = task.getDaysAdded();
        assertEquals(2, daysAdded);
    }

    @Test
    @DisplayName("Operations before save record or update record")
    void testPrePersistAndUpdateMethods() {
        Task spyTask = spy(task);

        doNothing().when(spyTask).onCreate();
        doNothing().when(spyTask).onUpdate();

        spyTask.onCreate();
        assertNotNull(spyTask.getDateAdded());
        assertNotNull(spyTask.getDateUpdated());

        spyTask.onUpdate();
        assertNotNull(spyTask.getDateUpdated());
    }
}





