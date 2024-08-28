package com.hftamayo.java.todo.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collections;

public class UserTest {

    private User user;
    private Roles role;

    @BeforeEach
    public void setUp() {
        role = Roles.builder()
                .id(1L)
                .roleEnum(ERole.ROLE_USER)
                .description("User role")
                .status(true)
                .build();

        user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .password("password")
                .age(30)
                .isAdmin(false)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .status(true)
                .dateAdded(LocalDateTime.now())
                .dateUpdated(LocalDateTime.now())
                .role(role)
                .build();
    }

    @Test
    public void testGettersAndSetters() {
        user.setName("Jane Doe");
        Assertions.assertEquals("Jane Doe", user.getName());

        user.setEmail("jane.doe@example.com");
        Assertions.assertEquals("jane.doe@example.com", user.getEmail());

        user.setPassword("newpassword");
        Assertions.assertEquals("newpassword", user.getPassword());

        user.setAge(25);
        Assertions.assertEquals(25, user.getAge());

        user.setAdmin(true);
        Assertions.assertTrue(user.isAdmin());

        user.setStatus(false);
        Assertions.assertFalse(user.isEnabled());
    }

    @Test
    public void testUserDetailsMethods() {
        Assertions.assertEquals("john.doe@example.com", user.getUsername());
        Assertions.assertEquals("password", user.getPassword());
        Assertions.assertTrue(user.isAccountNonExpired());
        Assertions.assertTrue(user.isAccountNonLocked());
        Assertions.assertTrue(user.isCredentialsNonExpired());
        Assertions.assertTrue(user.isEnabled());
        Assertions.assertEquals(Collections.singleton(new SimpleGrantedAuthority(role.getRoleEnum().name())),
                user.getAuthorities());
    }

    @Test
    public void testRoleRelationship() {
        Assertions.assertEquals(role, user.getRole());
    }

    @Test
    public void testAddTask() {
        Task task = Task.builder()
                .id(1L)
                .description("Sample Task")
                .status(true)
                .build();

        user.getTasks().add(task);
        task.setUser(user);

        Assertions.assertEquals(1, user.getTasks().size());
        Assertions.assertTrue(user.getTasks().contains(task));
    }

    @Test
    public void testRemoveTask() {
        Task task = Task.builder()
                .id(1L)
                .description("Sample Task")
                .status(true)
                .build();

        user.getTasks().add(task);
        task.setUser(user);

        user.getTasks().remove(task);
        task.setUser(null);

        Assertions.assertEquals(0, user.getTasks().size());
        Assertions.assertFalse(user.getTasks().contains(task));
    }

    @Test
    public void testUpdateUserRole() {
        Roles newRole = Roles.builder()
                .id(2L)
                .roleEnum(ERole.ROLE_ADMIN)
                .description("Admin role")
                .status(true)
                .build();

        user.setRole(newRole);

        Assertions.assertEquals(newRole, user.getRole());
    }

    @Test
    public void testUpdateAccountExpiry() {
        user.setAccountNonExpired(false);

        Assertions.assertFalse(user.isAccountNonExpired());
    }

    @Test
    public void testUpdateAccountLock() {
        user.setAccountNonLocked(false);

        Assertions.assertFalse(user.isAccountNonLocked());
    }
}