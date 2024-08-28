package com.hftamayo.java.todo.model;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

public class RolesTest {

    @Mock
    private User mockUser;

    @InjectMocks
    private Roles roles;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        roles = Roles.builder()
                .id(1L)
                .roleEnum(ERole.ROLE_ADMIN)
                .description("Administrator role")
                .status(true)
                .users(new HashSet<>())
                .build();
    }

    @Test
    public void testAddUser() {
        Set<User> users = new HashSet<>();
        users.add(mockUser);
        roles.setUsers(users);

        Assertions.assertEquals(1, roles.getUsers().size());
        Assertions.assertTrue(roles.getUsers().contains(mockUser));
    }

    @Test
    public void testRemoveUser() {
        Set<User> users = new HashSet<>();
        users.add(mockUser);
        roles.setUsers(users);

        roles.getUsers().remove(mockUser);

        Assertions.assertEquals(0, roles.getUsers().size());
        Assertions.assertFalse(roles.getUsers().contains(mockUser));
    }

    @Test
    public void testUpdateRoleStatus() {
        roles.setStatus(false);

        Assertions.assertFalse(roles.isStatus());
    }

    @Test
    public void testUpdateRoleDescription() {
        String newDescription = "Updated role description";
        roles.setDescription(newDescription);

        Assertions.assertEquals(newDescription, roles.getDescription());
    }
}