package com.hftamayo.java.todo.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(schema = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, name = "task_title", nullable = false)
    private String title;

    @Column(name = "task_description", nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime dateAdded;

    @Column(nullable = false)
    private LocalDateTime dateUpdated;

    /* for the next release
    @Column(name = "owner_id", nullable = false)
    private long createdBy;

    @Column
    private boolean isActive = Boolean.TRUE;
    para el resto de softDelete ver: https://www.baeldung.com/spring-jpa-soft-delete

     */

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

    public long getDaysAdded(){
        return ChronoUnit.DAYS.between(this.getDateAdded(), LocalDateTime.now());
    }
}
