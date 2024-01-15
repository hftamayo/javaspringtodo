package com.hftamayo.java.todo.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Column(nullable = false, unique = true, name = "user_name")
    private String name;

    @Column(nullable = false, unique = true, name = "user_email")
    private String email;

    @Column(nullable = false, name = "user_password")
    private String password;

    @Column(nullable = false, name = "user_age")
    private int age;

    @Column(nullable = false, name = "user_isadmin")
    private boolean isAdmin = false;

    @Column(nullable = false, name = "user_status")
    @Builder.Default
    private boolean userStatus = true;

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