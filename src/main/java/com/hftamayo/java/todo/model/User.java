package com.hftamayo.java.todo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hftamayo.java.todo.security.interfaces.RoleGrantedAuthority;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
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

    @Column(nullable = false, name= "isAccountNonExpired")
    @Builder.Default
    private boolean isAccountNonExpired = true;

    @Column(nullable = false, name= "isAccountNonLocked")
    @Builder.Default
    private boolean isAccountNonLocked = true;

    @Column(nullable = false, name= "isCredentialsNonExpired")
    @Builder.Default
    private boolean isCredentialsNonExpired = true;

    @Column(nullable = false, name = "user_status")
    @Builder.Default
    private boolean status = false;

    @Column
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "America/El_Salvador")
    private LocalDateTime dateAdded;

    @Column
    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "America/El_Salvador")
    private LocalDateTime dateUpdated;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            schema = "users",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Roles> roles;

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
        return roles.stream()
                .map(RoleGrantedAuthority::new)
                .collect(Collectors.toSet());
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