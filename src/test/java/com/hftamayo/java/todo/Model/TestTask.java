package com.hftamayo.java.todo.Model;

import org.assertj.core.api.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.*;

public class TestTask {

    @Test
    @DisplayName("New Task: sending valid values")
    public void givenValues_whenNewTask_thenAcceptStringFields(){
        Task testTask = new Task();
        testTask.setTitle("Supermarket list");
        testTask.setDescription("meat, cereal, cheese and milk");
        assertEquals("Supermarket list", testTask.getTitle());
        assertEquals("meat, cereal, cheese and milk", testTask.getDescription());
    }

    @Test
    @DisplayName("New Task: rejecting null values")
    public void givenNullValues_whenNewTask_thenRejectNulls(){
        Task testTask = new Task();
        testTask.setTitle("");
        testTask.setDescription("");
        assertNotNull(testTask.getTitle());
        assertNotNull(testTask.getDescription());
    }

    @Test
    @DisplayName("New task: checking dateAdded value")
    public void givenTaskCreationRequest_whenOnCreate_thenCheckDatedAdded(){
        Task newTask = new Task();
        newTask.onCreate();
        assertEquals(LocalDate.now(), newTask.getDateAdded());
    }

    @Test
    @DisplayName("New task: checking updateAdded value")
    public void givenTaskUpdateRequest_whenOnUpdate_thenCheckDatedUpdated(){
        Task newTask = new Task();
        newTask.onUpdate();
        assertEquals(LocalDate.now(), newTask.getDateUpdated());
    }

    @Test
    @DisplayName("Reject insert existing task")
    public void givenNewTitle_whenAlreadyExists_thenRejectTaskCreation(){
        Task testTask = new Task();
        testTask.setTitle("Supermarket list");
    }

    @Test
    @DisplayName("Get Days Added")
    public void givenNewTask_whenGetDaysAddedRequest_thenDisplayHowManyDaysAdded(){
        Clock clockDateNow;

        MockedStatic<LocalDateTime> localDateTimeMocked;

        ZoneId z = ZoneId.of("UTC");

        LocalDate ld = LocalDate.of(2023, Month.MARCH, 01);
        LocalTime lt = LocalTime.NOON;
        ZonedDateTime zdt = ZonedDateTime.of(ld, lt, z);
        Instant instant = zdt.toInstant();
        Clock clockDateAdded = Clock.fixed(instant, z);
        Instant instantDateAdded = Instant.now(clockDateAdded);

        localDateTimeMocked = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS);
        localDateTimeMocked.when(LocalDateTime::now).thenReturn(instantDateAdded);

        System.out.println("Fecha de agregacion: "+LocalDateTime.now());


        /*
        final String FAKE_DATE_ADDED = "2023-03-01T00:00:00.00Z";
        final String FAKE_DATE_NOW  = "2023-06-01T00:00:00.00Z";

        clockDateAdded = Clock.fixed(Instant.parse(FAKE_DATE_ADDED), ZoneId.of("UTC"));
        LocalDateTime localDateTimeAdded = LocalDateTime.now(clockDateAdded);

        Mockito.when(LocalDate.now()).thenReturn(LocalDate.from(localDateTimeAdded));
        Task testTask = new Task();
        testTask.onCreate();

        clockDateNow = Clock.fixed(Instant.parse(FAKE_DATE_NOW), ZoneId.of("UTC"));
        LocalDateTime localDateTimeNow = LocalDateTime.now(clockDateNow);
        Mockito.when(LocalDate.now()).thenReturn(LocalDate.from(localDateTimeNow));

        assertEquals(testTask.getDaysAdded(), 92);

         */
    }
}
