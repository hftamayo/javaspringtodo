package com.hftamayo.java.todo.Model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(schema = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate dateAdded;

    @Column(nullable = false)
    private LocalDate dateUpdated;

    public Task() {

    }

    @PrePersist
    protected void onCreate(){
        dateAdded = LocalDate.now();
        dateUpdated = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate(){
        dateUpdated = LocalDate.now();
    }

    public long getId() {
        return id;
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

    public LocalDate getDateUpdated() {
        return dateUpdated;
    }

    public long getDaysAdded(){
        return ChronoUnit.DAYS.between(this.getDateAdded(), LocalDate.now());
    }
}
