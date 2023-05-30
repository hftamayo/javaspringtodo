package com.hftamayo.java.todo.Model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(schema = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String title;

    @Column
    private String description;

    @Column
    private LocalDate dateAdded;

    @Transient
    private int daysAdded;

    public Task(long id, String title, String description, LocalDate dateAdded) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dateAdded = dateAdded;
    }

    public Task() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public int getDaysAdded(){
        return Period.between(this.dateAdded,LocalDate.now()).getYears();
    }

    public void setDaysAdded(int daysAdded){
        this.daysAdded = daysAdded;
    }
}
