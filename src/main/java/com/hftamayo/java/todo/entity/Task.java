package com.hftamayo.java.todo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, name = "task_title")
    private String title;

    @Column(nullable = false, name = "task_description")
    private String description;

    @Column(nullable = false, name = "status")
    @Builder.Default
    private boolean status = true;

    @Column
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "America/El_Salvador")
    private LocalDateTime dateAdded;

    @Column
    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "America/El_Salvador")
    private LocalDateTime dateUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    /* for the next release
    @Column(name = "owner_id", nullable = false)
    private long taskOwner;

    @Column
    @CreatedBy
    User owner

    para el resto de softDelete ver: https://www.baeldung.com/spring-jpa-soft-delete
     */

//    @PrePersist
//    protected void onCreate(){
//        dateAdded = LocalDateTime.now();
//        dateUpdated = LocalDateTime.now();
//        //createdBy = LoggedUser.getId();
//    }
//
//    @PreUpdate
//    protected void onUpdate(){
//        dateUpdated = LocalDateTime.now();
//        //updatedBy = LoggedUser.getId();
//    }

    public long getDaysAdded(){
        return ChronoUnit.DAYS.between(this.getDateAdded(), LocalDateTime.now());
    }
}
