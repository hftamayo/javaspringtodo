package com.hftamayo.java.todo.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Value;

@Data
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
    @NotBlank
    private String title;

    @Column(name = "task_description")
    @NotBlank
    private String description;

    @Column
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm", timezone = "America/El_Salvador")
    private LocalDateTime dateAdded;

    @Column
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm", timezone = "America/El_Salvador")
    private LocalDateTime dateUpdated;

    @Column(name = "status")
    @NonNull
    @Value("true")
    private boolean status;

    /* for the next release
    @Column(name = "owner_id", nullable = false)
    private long taskOwner;

    @Column
    @CreatedDate
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm", timezone = "America/El_Salvador")
    private LocalDateTime dateAdded;

    @Column
    @CreatedBy
    User owner

    @Column
    @LastModifiedDate
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm", timezone = "America/El_Salvador")
    private LocalDateTime dateUpdated;

    @Column
    @LastModifiedBy
    User modifiedBy


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
