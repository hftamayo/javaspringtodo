package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.model.User;
import com.hftamayo.java.todo.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private UserController userController;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = Mockito.mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    public void testGetUsers() {
        UserResponseDto user = new UserResponseDto();
        when(userService.getUsers()).thenReturn(Collections.singletonList(user));

        List<UserResponseDto> users = userController.getUsers();
        assertEquals(1, users.size());
        verify(userService, times(1)).getUsers();
    }

    @Test
    public void testGetUser() {
        UserResponseDto user = new UserResponseDto();
        when(userService.getUser(1L)).thenReturn(Optional.of(user));

        Optional<UserResponseDto> result = userController.getUser(1L);
        assertTrue(result.isPresent());
        verify(userService, times(1)).getUser(1L);
    }

    @Test
    public void testGetUserByCriteria() {
        UserResponseDto user = new UserResponseDto();
        when(userService.getUserByCriteria("name", "John"))
                .thenReturn(Optional.of(Collections.singletonList(user)));

        Optional<List<UserResponseDto>> result = userController.getUserByCriteria("name", "John");
        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        verify(userService, times(1)).getUserByCriteria("name", "John");
    }

    @Test
    public void testGetUserByCriterias() {
        UserResponseDto user = new UserResponseDto();
        when(userService.getUserByCriterias("name", "John", "status", "active"))
                .thenReturn(Optional.of(Collections.singletonList(user)));

        Optional<List<UserResponseDto>> result = userController
                .getUserByCriterias("name", "John", "status", "active");
        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        verify(userService, times(1))
                .getUserByCriterias("name", "John", "status", "active");
    }

    @Test
    public void testSaveUser() {
        User user = new User();
        UserResponseDto userResponse = new UserResponseDto();
        when(userService.saveUser(user)).thenReturn(userResponse);

        UserResponseDto result = userController.saveUser(user);
        assertEquals(userResponse, result);
        verify(userService, times(1)).saveUser(user);
    }

    @Test
    public void testUpdateUser_Success() {
        User user = new User();
        UserResponseDto userResponse = new UserResponseDto();
        when(userService.updateUser(1L, user)).thenReturn(userResponse);

        UserResponseDto result = userController.updateUser(1L, user);
        assertEquals(userResponse, result);
        verify(userService, times(1)).updateUser(1L, user);
    }

    @Test
    public void testUpdateUser_NotFound() {
        User user = new User();
        when(userService.updateUser(1L, user)).thenThrow(new EntityNotFoundException());

        assertThrows(ResponseStatusException.class, () -> userController.updateUser(1L, user));
        verify(userService, times(1)).updateUser(1L, user);
    }

    @Test
    public void testUpdateUserStatus_Success() {
        UserResponseDto userResponse = new UserResponseDto();
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", true);
        when(userService.updateUserStatus(1L, true)).thenReturn(userResponse);

        UserResponseDto result = userController.updateUserStatus(1L, updates);
        assertEquals(userResponse, result);
        verify(userService, times(1)).updateUserStatus(1L, true);
    }

    @Test
    public void testUpdateUserStatus_NotFound() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", true);
        when(userService.updateUserStatus(1L, true)).thenThrow(new EntityNotFoundException());

        assertThrows(ResponseStatusException.class, () -> userController.updateUserStatus(1L, updates));
        verify(userService, times(1)).updateUserStatus(1L, true);
    }

    @Test
    public void testUpdateUserStatusAndRole_Success() {
        UserResponseDto userResponse = new UserResponseDto();
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", true);
        updates.put("role", "ROLE_ADMIN");
        when(userService.updateUserStatusAndRole(1L, true, "ROLE_ADMIN"))
                .thenReturn(userResponse);

        UserResponseDto result = userController.updateUserStatusAndRole(1L, updates);
        assertEquals(userResponse, result);
        verify(userService, times(1))
                .updateUserStatusAndRole(1L, true, "ROLE_ADMIN");
    }

    @Test
    public void testUpdateUserStatusAndRole_NotFound() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", true);
        updates.put("role", "ROLE_ADMIN");
        when(userService.updateUserStatusAndRole(1L, true, "ROLE_ADMIN"))
                .thenThrow(new EntityNotFoundException());

        assertThrows(ResponseStatusException.class, () -> userController
                .updateUserStatusAndRole(1L, updates));
        verify(userService, times(1))
                .updateUserStatusAndRole(1L, true, "ROLE_ADMIN");
    }

    @Test
    public void testDeleteUser_Success() {
        doNothing().when(userService).deleteUser(1L);

        ResponseEntity<?> response = userController.deleteUser(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    public void testDeleteUser_NotFound() {
        doThrow(new EntityNotFoundException()).when(userService).deleteUser(1L);

        ResponseEntity<?> response = userController.deleteUser(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    public void testDeleteUser_InternalServerError() {
        doThrow(new RuntimeException()).when(userService).deleteUser(1L);

        ResponseEntity<?> response = userController.deleteUser(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(userService, times(1)).deleteUser(1L);
    }
}