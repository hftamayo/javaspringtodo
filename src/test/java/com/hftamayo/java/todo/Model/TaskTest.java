//package com.hftamayo.java.todo.Model;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockedStatic;
//import org.mockito.Mockito;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//import java.time.*;
//
//public class TaskTest {
//    MockedStatic<LocalDateTime> localDateTimeMocked;
//
//    @Test
//    @DisplayName("New Task: sending valid values")
//    public void givenValues_whenNewTask_thenAcceptStringFields(){
//        Task testTask = new Task();
//        testTask.setTitle("Supermarket list");
//        testTask.setDescription("meat, cereal, cheese and milk");
//        assertEquals("Supermarket list", testTask.getTitle());
//        assertEquals("meat, cereal, cheese and milk", testTask.getDescription());
//    }
//
//    @Test
//    @DisplayName("New Task: rejecting null values")
//    public void givenNullValues_whenNewTask_thenRejectNulls(){
//        Task testTask = new Task();
//        testTask.setTitle("");
//        testTask.setDescription("");
//        assertNotNull(testTask.getTitle());
//        assertNotNull(testTask.getDescription());
//    }
//
//    @Test
//    @DisplayName("New task: checking dateAdded value")
//    public void givenTaskCreationRequest_whenOnCreate_thenCheckDatedAdded(){
//        Task newTask = new Task();
//        newTask.onCreate();
//        assertEquals(LocalDate.now(), newTask.getDateAdded().toLocalDate());
//    }
//
//    @Test
//    @DisplayName("New task: checking updateAdded value")
//    public void givenTaskUpdateRequest_whenOnUpdate_thenCheckDatedUpdated(){
//        Task newTask = new Task();
//        newTask.onUpdate();
//        assertEquals(LocalDate.now(), newTask.getDateUpdated().toLocalDate());
//    }
//
//    @Test
//    @DisplayName("Reject insert existing task's title")
//    public void givenNewTitle_whenAlreadyExists_thenRejectTaskCreation(){
//        Task firstTestTask = new Task();
//        firstTestTask.setTitle("Supermarket list");
//        firstTestTask.setDescription("meat, cereal, cheese and milk");
//        Task secondTestTask = new Task();
//        secondTestTask.setTitle("Supermarket list");
//        secondTestTask.setDescription("fruits, veggies");
//        //please polish the following case evaluation:
//        Assertions.assertFalse(firstTestTask.getTitle() != secondTestTask.getTitle());
//
//    }
//
//    @Test
//    @DisplayName("Get Days Added")
//    public void givenNewTask_whenGetDaysAddedRequest_thenDisplayHowManyDaysAdded(){
//        localDateTimeMocked = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS);
//        LocalDateTime fakeDateAdded = LocalDateTime.of(2023, 03, 01, 12, 0);
//        localDateTimeMocked.when(LocalDateTime::now).thenReturn(fakeDateAdded);
//
//        Task testTask = new Task();
//        testTask.onCreate();
//
//        LocalDateTime fakeDateNow = LocalDateTime.of(2023, 06, 01, 12, 0);
//        localDateTimeMocked.when(LocalDateTime::now).thenReturn(fakeDateNow);
//
//        assertEquals(testTask.getDaysAdded(), 92);
//    }
//}
