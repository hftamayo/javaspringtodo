package com.hftamayo.java.todo.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(schema = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, name = "user_name")
    @NonNull
    private String name;

    @Column(name = "user_email")
    @NonNull
    private String email;

    @Column(name = "user_password")
    @NonNull
    private String password;

    @Column(name = "user_age")
    @NonNull
    private int age;

    @Column(name = "user_isadmin")
    @NonNull
    private boolean isadmin = false;

    @Column(name = "status")
    @NonNull
    @Builder.Default
    private boolean status = true;

    @Column
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm", timezone = "America/El_Salvador")
    private LocalDateTime dateAdded;

    @Column
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm", timezone = "America/El_Salvador")
    private LocalDateTime dateUpdated;

    @PrePersist
    protected void onCreate() {
        dateAdded = LocalDateTime.now();
        dateUpdated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dateUpdated = LocalDateTime.now();
    }

}