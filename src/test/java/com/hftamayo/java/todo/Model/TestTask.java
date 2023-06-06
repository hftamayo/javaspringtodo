package com.hftamayo.java.todo.Model;

import org.assertj.core.api.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class TestTask {

    @Test
    @DisplayName("Date Added value on Create")
    public void givenProperties_whenNewTask_thenCheckDateAdded(){
        Task testTask = new Task("Supermarket List", "meat, cereal, cheese and milk");
        Assert.equals(LocalDate.now(), testTask.getDateAdded());
    }

    @Test
    @DisplayName("Date Updated value on Create")
    public void givenProperties_whenNewTask_thenCheckDateUpdated(){
        Task testTask = new Task("Supermarket List", "meat, cereal, cheese and milk");
        Assert.equals(LocalDate.now(), testTask.getDateUpdated());
    }    
}
