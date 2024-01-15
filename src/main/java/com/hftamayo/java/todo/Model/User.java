package com.hftamayo.java.todo.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Set;

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
    @CreatedDate
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm", timezone = "America/El_Salvador")
    private LocalDateTime dateAdded;

    @Column
    @LastModifiedDate
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm", timezone = "America/El_Salvador")
    private LocalDateTime dateUpdated;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            schema = "users",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Roles> roles;

//    @PrePersist
//    protected void onCreate() {
//        dateAdded = LocalDateTime.now();
//        dateUpdated = LocalDateTime.now();
//    }
//
//    @PreUpdate
//    protected void onUpdate() {
//        dateUpdated = LocalDateTime.now();
//    }
}