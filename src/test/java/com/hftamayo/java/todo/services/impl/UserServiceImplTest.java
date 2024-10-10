package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dao.RolesDao;
import com.hftamayo.java.todo.dao.UserDao;
import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.exceptions.EntityAlreadyExistsException;
import com.hftamayo.java.todo.model.Roles;
import com.hftamayo.java.todo.model.User;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    private UserServiceImpl userService;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private RolesDao rolesDao;

    @BeforeEach
    public void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        rolesDao = Mockito.mock(RolesDao.class);
        userService = new UserServiceImpl(userDao, passwordEncoder, rolesDao);
    }

    @Test
    public void testGetUsers() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setAge(30);
        user.setAdmin(true);
        user.setStatus(true);
        Date date = new Date();
        LocalDateTime localDateTime = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        user.setDateAdded(localDateTime);

        when(userDao.getUsers()).thenReturn(Collections.singletonList(user));

        List<UserResponseDto> users = userService.getUsers();
        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertEquals("John Doe", users.get(0).getName());
    }

    @Test
    public void testGetUser() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setAge(30);
        user.setAdmin(true);
        user.setStatus(true);
        Date date = new Date();
        LocalDateTime localDateTime = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        user.setDateAdded(localDateTime);

        when(userDao.getUserById(1L)).thenReturn(Optional.of(user));

        Optional<UserResponseDto> foundUser = userService.getUser(1L);
        assertTrue(foundUser.isPresent());
        assertEquals("John Doe", foundUser.get().getName());
    }

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");
        user.setAge(30);
        user.setAdmin(true);
        user.setStatus(true);
        Date date = new Date();
        LocalDateTime localDateTime = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        user.setDateAdded(localDateTime);

        when(userDao.getUserByCriteria("email", "john.doe@example.com", true)).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userDao.saveUser(any(User.class))).thenReturn(user);

        UserResponseDto savedUser = userService.saveUser(user);
        assertEquals("John Doe", savedUser.getName());
    }

    @Test
    public void testSaveUserAlreadyExists() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");
        user.setAge(30);
        user.setAdmin(true);
        user.setStatus(true);
        Date date = new Date();
        LocalDateTime localDateTime = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        user.setDateAdded(localDateTime);

        when(userDao.getUserByCriteria("email", "john.doe@example.com", true)).thenReturn(Optional.of(user));

        assertThrows(EntityAlreadyExistsException.class, () -> userService.saveUser(user));
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");
        user.setAge(30);
        user.setAdmin(true);
        user.setStatus(true);
        Date date = new Date();
        LocalDateTime localDateTime = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        user.setDateAdded(localDateTime);

        when(userDao.getUserById(1L)).thenReturn(Optional.of(user));
        when(userDao.updateUser(anyLong(), anyMap())).thenReturn(user);

        User updatedUser = new User();
        updatedUser.setName("Jane Doe");
        updatedUser.setEmail("jane.doe@example.com");
        updatedUser.setPassword("newPassword");
        updatedUser.setAge(25);
        updatedUser.setAdmin(false);
        updatedUser.setStatus(false);

        UserResponseDto updatedUserDto = userService.updateUser(1L, updatedUser);
        assertEquals("Jane Doe", updatedUserDto.getName());
        assertEquals("jane.doe@example.com", updatedUserDto.getEmail());
    }

    @Test
    public void testUpdateUserNotFound() {
        when(userDao.getUserById(1L)).thenReturn(Optional.empty());

        User updatedUser = new User();
        updatedUser.setName("Jane Doe");
        updatedUser.setEmail("jane.doe@example.com");
        updatedUser.setPassword("newPassword");
        updatedUser.setAge(25);
        updatedUser.setAdmin(false);
        updatedUser.setStatus(false);

        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(1L, updatedUser));
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setAge(30);
        user.setAdmin(true);
        user.setStatus(true);
        Date date = new Date();
        LocalDateTime localDateTime = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        user.setDateAdded(localDateTime);

        when(userDao.getUserById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userDao).deleteUser(1L);

        userService.deleteUser(1L);
        verify(userDao, times(1)).deleteUser(1L);
    }

    @Test
    public void testDeleteUserNotFound() {
        when(userDao.getUserById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(1L));
    }
}