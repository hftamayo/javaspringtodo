package com.hftamayo.java.todo.Model;

import org.assertj.core.api.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

public class TestTask {

    @Test
    @DisplayName("New Task: not null fields")
    public void givenValues_whenNewTask_thenAcceptStringFields(){
        Task testTask = new Task();
        testTask.setTitle("Supermarket list");
        testTask.setDescription("meat, cereal, cheese and milk");
        assertEquals("Supermarket list", testTask.getTitle());
        assertEquals("meat, cereal, cheese and milk", testTask.getDescription());
    }

    @Test
    @DisplayName("New Task: not null fields")
    public void givenNullValues_whenNewTask_thenRejectNulls(){
        Task testTask = new Task();
        testTask.setTitle("");
        testTask.setDescription("");
        assertNotNull(testTask.getTitle());
        assertNotNull(testTask.getDescription());
    }
}
