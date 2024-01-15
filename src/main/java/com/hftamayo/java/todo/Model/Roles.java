package com.hftamayo.java.todo.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(schema = "roles")
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true, name = "role_name")
    private String name;

    @Column(nullable = false, name = "role_description")
    private String description;

    @Column(nullable = false, name = "role_status")
    @Builder.Default
    private boolean status = true;

    @Column
    @CreatedDate
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm", timezone = "America/El_Salvador")
    private LocalDateTime dateAdded;

    @Column
    @LastModifiedDate
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm", timezone = "America/El_Salvador")
    private LocalDateTime dateUpdated;
}
