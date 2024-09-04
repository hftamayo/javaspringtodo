package com.hftamayo.java.todo.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class TaskTest {
    private Task task;
    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .password("password")
                .age(30)
                .isAdmin(false)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .status(true)
                .dateAdded(LocalDateTime.now())
                .dateUpdated(LocalDateTime.now())
                .build();

        task = Task.builder()
                .id(1L)
                .description("Sample Task")
                .status(true)
                .dateAdded(LocalDateTime.now())
                .dateUpdated(LocalDateTime.now())
                .user(user)
                .build();
    }

    @Test
    public void testGettersAndSetters() {
        task.setDescription("Updated Task");
        Assertions.assertEquals("Updated Task", task.getDescription());

        task.setStatus(false);
        Assertions.assertFalse(task.isStatus());
    }

    @Test
    public void testUserRelationship() {
        Assertions.assertEquals(user, task.getUser());
    }

    @Test
    public void testUpdateTaskStatus() {
        task.setStatus(false);
        Assertions.assertFalse(task.isStatus());
    }

    @Test
    public void testUpdateTaskDescription() {
        String newDescription = "Updated Task Description";
        task.setDescription(newDescription);
        Assertions.assertEquals(newDescription, task.getDescription());
    }

    @Test
    public void testUpdateTaskDates() {
        LocalDateTime newDate = LocalDateTime.now().plusDays(1);
        task.setDateAdded(newDate);
        task.setDateUpdated(newDate);
        Assertions.assertEquals(newDate, task.getDateAdded());
        Assertions.assertEquals(newDate, task.getDateUpdated());
    }
}
