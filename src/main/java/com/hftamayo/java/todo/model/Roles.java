package com.hftamayo.java.todo.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "roles")
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "role_enum")
    private ERole roleEnum;

    @NonNull
    @Column(nullable = false, name = "role_description")
    private String description;

    @NonNull
    @Column(nullable = false, name = "role_status")
    @Builder.Default
    private boolean status = true;

    @NonNull
    @Column
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "America/El_Salvador")
    private LocalDateTime dateAdded;

    @NonNull
    @Column
    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "America/El_Salvador")
    private LocalDateTime dateUpdated;

    @Builder.Default
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<User> users = new HashSet<>();
}
