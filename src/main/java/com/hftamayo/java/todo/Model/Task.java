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

    @Column(unique = true, name = "task_title")
    @NonNull
    private String title;

    @Column(name = "task_description")
    @NonNull
    private String description;

    @Column
    @NonNull
    private LocalDateTime dateAdded;

    @Column
    @NonNull
    private LocalDateTime dateUpdated;

    @Column(name = "status")
    @NonNull
    private boolean isActive = Boolean.TRUE;

    /* for the next release
    @Column(name = "owner_id", nullable = false)
    private long taskOwner;

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
