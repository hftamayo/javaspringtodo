package com.hftamayo.java.todo.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
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
    private LocalDateTime dateAdded;

    /* for the next release
    @Column(nullable = false)
    private long createdBy;
     */

    @Column(nullable = false)
    private LocalDateTime dateUpdated;

    /* for the next release
    @Column
    private long updatedBy;
     */

    /*

    @Column
    private boolean isActive = Boolean.TRUE;
    GENERAR GETTERS Y SETTERS PARA LAS NUEVAS PROPIEDADES
    para el resto de softDelete ver: https://www.baeldung.com/spring-jpa-soft-delete
     */
    public Task() {

    }

    @PrePersist
    protected void onCreate(){
        dateAdded = LocalDateTime.now();
        dateUpdated = LocalDateTime.now();
        //createdBy = LoggedUser.getId();
    }

    @PreUpdate
    protected void onUpdate(){
        dateUpdated = LocalDateTime.now();
        //updatedBy = LoggedUser.getId();
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

    public LocalDateTime getDateAdded() {
        return dateAdded;
    }

    public LocalDateTime getDateUpdated() {
        return dateUpdated;
    }

    public long getDaysAdded(){
        return ChronoUnit.DAYS.between(this.getDateAdded(), LocalDateTime.now());
    }
}
