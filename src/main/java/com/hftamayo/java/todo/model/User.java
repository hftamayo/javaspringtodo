package com.hftamayo.java.todo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity(name = "User")
@Table(schema = "users")
public class User implements UserDetails {

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
    @Builder.Default
    private boolean isAdmin = false;

    @Column(nullable = false, name = "isAccountNonExpired")
    @Builder.Default
    private boolean isAccountNonExpired = true;

    @Column(nullable = false, name = "isAccountNonLocked")
    @Builder.Default
    private boolean isAccountNonLocked = true;

    @Column(nullable = false, name = "isCredentialsNonExpired")
    @Builder.Default
    private boolean isCredentialsNonExpired = true;

    @Column(nullable = false, name = "user_status")
    @Builder.Default
    private boolean status = false;

    @Column
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "America/El_Salvador")
    private LocalDateTime dateAdded;

    @Column
    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "America/El_Salvador")
    private LocalDateTime dateUpdated;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(
            name = "role_id",
            referencedColumnName = "id"
    )
    private Roles role;

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.status;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.getRoleEnum().name()));
    }

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