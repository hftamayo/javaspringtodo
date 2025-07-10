package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.CrudOperationResponseDto;
import com.hftamayo.java.todo.dto.pagination.PageRequestDto;
import com.hftamayo.java.todo.dto.pagination.PageResponseDto;
import com.hftamayo.java.todo.dto.roles.RolesResponseDto;
import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.entity.User;
import com.hftamayo.java.todo.exceptions.ResourceNotFoundException;
import com.hftamayo.java.todo.exceptions.ValidationException;
import com.hftamayo.java.todo.exceptions.DuplicateResourceException;
import com.hftamayo.java.todo.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void getUsers_WhenUsersExist_ShouldReturnSuccessResponse() {
        CrudOperationResponseDto<UserResponseDto> expectedResponse = new CrudOperationResponseDto<>();
        expectedResponse.setCode(200);
        expectedResponse.setResultMessage("OPERATION SUCCESSFUL");
        expectedResponse.setDataList(List.of(new UserResponseDto()));

        when(userService.getUsers()).thenReturn(expectedResponse);

        CrudOperationResponseDto<UserResponseDto> response = userController.getUsers();

        assertAll(
                () -> assertEquals(200, response.getCode()),
                () -> assertEquals("OPERATION SUCCESSFUL", response.getResultMessage()),
                () -> assertEquals(1, response.getDataList().size()),
                () -> verify(userService).getUsers()
        );
    }

    @Test
    void getUser_WhenUserExists_ShouldReturnSuccessResponse() {
        long userId = 1L;
        CrudOperationResponseDto<UserResponseDto> expectedResponse = new CrudOperationResponseDto<>();
        expectedResponse.setCode(200);
        expectedResponse.setResultMessage("OPERATION SUCCESSFUL");
        expectedResponse.setData(new UserResponseDto());

        when(userService.getUser(userId)).thenReturn(expectedResponse);

        CrudOperationResponseDto<UserResponseDto> response = userController.getUser(userId);

        assertAll(
                () -> assertEquals(200, response.getCode()),
                () -> assertEquals("OPERATION SUCCESSFUL", response.getResultMessage()),
                () -> assertNotNull(response.getData()),
                () -> verify(userService).getUser(userId)
        );
    }

    @Test
    void getUser_WhenUserNotFound_ShouldThrowResourceNotFoundException() {
        long userId = 999L;
        when(userService.getUser(userId)).thenThrow(new ResourceNotFoundException("User not found with id: " + userId));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
            () -> userController.getUser(userId));
        
        assertEquals("User not found with id: 999", exception.getMessage());
        verify(userService).getUser(userId);
    }

    @Test
    void getUserByCriteria_WhenUserExists_ShouldReturnSuccessResponse() {
        String criteria = "email";
        String value = "test@example.com";
        CrudOperationResponseDto<UserResponseDto> expectedResponse = new CrudOperationResponseDto<>();
        expectedResponse.setCode(200);
        expectedResponse.setResultMessage("OPERATION SUCCESSFUL");
        expectedResponse.setDataList(List.of(new UserResponseDto()));

        when(userService.getUserByCriteria(criteria, value)).thenReturn(expectedResponse);

        CrudOperationResponseDto<UserResponseDto> response = userController.getUserByCriteria(criteria, value);

        assertAll(
                () -> assertEquals(200, response.getCode()),
                () -> assertEquals("OPERATION SUCCESSFUL", response.getResultMessage()),
                () -> assertEquals(1, response.getDataList().size()),
                () -> verify(userService).getUserByCriteria(criteria, value)
        );
    }

    @Test
    void getUserByCriteria_WhenInvalidCriteria_ShouldThrowValidationException() {
        String criteria = "invalid";
        String value = "test@example.com";
        when(userService.getUserByCriteria(criteria, value)).thenThrow(new ValidationException("Invalid criteria: " + criteria));

        ValidationException exception = assertThrows(ValidationException.class, 
            () -> userController.getUserByCriteria(criteria, value));
        
        assertEquals("Invalid criteria: invalid", exception.getMessage());
        verify(userService).getUserByCriteria(criteria, value);
    }

    @Test
    void getUserByCriterias_WhenUserExists_ShouldReturnSuccessResponse() {
        // Arrange
        String criteria = "email";
        String value = "test@example.com";
        String criteria2 = "status";
        String value2 = "active";
        CrudOperationResponseDto<UserResponseDto> expectedResponse = new CrudOperationResponseDto<>();
        expectedResponse.setCode(200);
        expectedResponse.setResultMessage("OPERATION SUCCESSFUL");
        expectedResponse.setDataList(List.of(new UserResponseDto()));

        when(userService.getUserByCriterias(criteria, value, criteria2, value2)).thenReturn(expectedResponse);

        CrudOperationResponseDto<UserResponseDto> response = userController.getUserByCriterias(criteria, value, criteria2, value2);

        assertAll(
                () -> assertEquals(200, response.getCode()),
                () -> assertEquals("OPERATION SUCCESSFUL", response.getResultMessage()),
                () -> assertEquals(1, response.getDataList().size()),
                () -> verify(userService).getUserByCriterias(criteria, value, criteria2, value2)
        );
    }

    @Test
    void saveUser_WhenValidUser_ShouldReturnSuccessResponse() {
        User user = new User();
        CrudOperationResponseDto<UserResponseDto> expectedResponse = new CrudOperationResponseDto<>();
        expectedResponse.setCode(201);
        expectedResponse.setResultMessage("OPERATION SUCCESSFUL");
        expectedResponse.setData(new UserResponseDto());

        when(userService.saveUser(user)).thenReturn(expectedResponse);

        CrudOperationResponseDto<UserResponseDto> response = userController.saveUser(user);

        assertAll(
                () -> assertEquals(201, response.getCode()),
                () -> assertEquals("OPERATION SUCCESSFUL", response.getResultMessage()),
                () -> assertNotNull(response.getData()),
                () -> verify(userService).saveUser(user)
        );
    }

    @Test
    void saveUser_WhenInvalidUser_ShouldThrowValidationException() {
        User user = new User();
        when(userService.saveUser(user)).thenThrow(new ValidationException("User email is required"));

        ValidationException exception = assertThrows(ValidationException.class, 
            () -> userController.saveUser(user));
        
        assertEquals("User email is required", exception.getMessage());
        verify(userService).saveUser(user);
    }

    @Test
    void saveUser_WhenDuplicateEmail_ShouldThrowDuplicateResourceException() {
        User user = new User();
        user.setEmail("existing@example.com");
        when(userService.saveUser(user)).thenThrow(new DuplicateResourceException("User with email already exists"));

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, 
            () -> userController.saveUser(user));
        
        assertEquals("User with email already exists", exception.getMessage());
        verify(userService).saveUser(user);
    }

    @Test
    void updateUser_WhenUserExists_ShouldReturnSuccessResponse() {
        long userId = 1L;
        User user = new User();
        CrudOperationResponseDto<UserResponseDto> expectedResponse = new CrudOperationResponseDto<>();
        expectedResponse.setCode(200);
        expectedResponse.setResultMessage("OPERATION SUCCESSFUL");
        expectedResponse.setData(new UserResponseDto());

        when(userService.updateUser(userId, user)).thenReturn(expectedResponse);

        CrudOperationResponseDto<UserResponseDto> response = userController.updateUser(userId, user);

        assertAll(
                () -> assertEquals(200, response.getCode()),
                () -> assertEquals("OPERATION SUCCESSFUL", response.getResultMessage()),
                () -> assertNotNull(response.getData()),
                () -> verify(userService).updateUser(userId, user)
        );
    }

    @Test
    void updateUser_WhenUserNotFound_ShouldThrowResourceNotFoundException() {
        long userId = 999L;
        User user = new User();
        when(userService.updateUser(userId, user)).thenThrow(new ResourceNotFoundException("User not found with id: " + userId));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
            () -> userController.updateUser(userId, user));
        
        assertEquals("User not found with id: 999", exception.getMessage());
        verify(userService).updateUser(userId, user);
    }

    @Test
    void updateUserStatus_WhenUserExists_ShouldReturnSuccessResponse() {
        // Arrange
        long userId = 1L;
        Map<String, Object> updates = Map.of("status", true);
        CrudOperationResponseDto<UserResponseDto> expectedResponse = new CrudOperationResponseDto<>();
        expectedResponse.setCode(200);
        expectedResponse.setResultMessage("OPERATION SUCCESSFUL");
        expectedResponse.setData(new UserResponseDto());

        when(userService.updateUserStatus(userId, true)).thenReturn(expectedResponse);

        CrudOperationResponseDto<UserResponseDto> response = userController.updateUserStatus(userId, updates);

        assertAll(
                () -> assertEquals(200, response.getCode()),
                () -> assertEquals("OPERATION SUCCESSFUL", response.getResultMessage()),
                () -> assertNotNull(response.getData()),
                () -> verify(userService).updateUserStatus(userId, true)
        );
    }

    @Test
    void updateUserStatus_WhenUserNotFound_ShouldThrowResourceNotFoundException() {
        long userId = 999L;
        Map<String, Object> updates = Map.of("status", true);
        when(userService.updateUserStatus(userId, true)).thenThrow(new ResourceNotFoundException("User not found with id: " + userId));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
            () -> userController.updateUserStatus(userId, updates));
        
        assertEquals("User not found with id: 999", exception.getMessage());
        verify(userService).updateUserStatus(userId, true);
    }

    @Test
    void updateUserStatusAndRole_WhenUserExists_ShouldReturnSuccessResponse() {
        long userId = 1L;
        Map<String, Object> updates = Map.of(
                "status", true,
                "role", "ROLE_ADMIN"
        );
        CrudOperationResponseDto<UserResponseDto> expectedResponse = new CrudOperationResponseDto<>();
        expectedResponse.setCode(200);
        expectedResponse.setResultMessage("OPERATION SUCCESSFUL");
        expectedResponse.setData(new UserResponseDto());

        when(userService.updateUserStatusAndRole(userId, true, "ROLE_ADMIN")).thenReturn(expectedResponse);

        CrudOperationResponseDto<UserResponseDto> response = userController.updateUserStatusAndRole(userId, updates);

        assertAll(
                () -> assertEquals(200, response.getCode()),
                () -> assertEquals("OPERATION SUCCESSFUL", response.getResultMessage()),
                () -> assertNotNull(response.getData()),
                () -> verify(userService).updateUserStatusAndRole(userId, true, "ROLE_ADMIN")
        );
    }

    @Test
    void updateUserStatusAndRole_WhenUserNotFound_ShouldThrowResourceNotFoundException() {
        long userId = 999L;
        Map<String, Object> updates = Map.of(
                "status", true,
                "role", "ROLE_ADMIN"
        );
        when(userService.updateUserStatusAndRole(userId, true, "ROLE_ADMIN")).thenThrow(new ResourceNotFoundException("User not found with id: " + userId));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
            () -> userController.updateUserStatusAndRole(userId, updates));
        
        assertEquals("User not found with id: 999", exception.getMessage());
        verify(userService).updateUserStatusAndRole(userId, true, "ROLE_ADMIN");
    }

    @Test
    void deleteUser_WhenUserExists_ShouldReturnSuccessResponse() {
        long userId = 1L;
        CrudOperationResponseDto<UserResponseDto> expectedResponse = new CrudOperationResponseDto<>();
        expectedResponse.setCode(200);
        expectedResponse.setResultMessage("OPERATION SUCCESSFUL");

        when(userService.deleteUser(userId)).thenReturn(expectedResponse);

        CrudOperationResponseDto<UserResponseDto> response = userController.deleteUser(userId);

        assertAll(
                () -> assertEquals(200, response.getCode()),
                () -> assertEquals("OPERATION SUCCESSFUL", response.getResultMessage()),
                () -> verify(userService).deleteUser(userId)
        );
    }

    @Test
    void deleteUser_WhenUserNotFound_ShouldThrowResourceNotFoundException() {
        long userId = 999L;
        when(userService.deleteUser(userId)).thenThrow(new ResourceNotFoundException("User not found with id: " + userId));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
            () -> userController.deleteUser(userId));
        
        assertEquals("User not found with id: 999", exception.getMessage());
        verify(userService).deleteUser(userId);
    }

    @Test
    void getUsers_WhenUsersExist_ShouldReturnPaginatedResponse() {
        PageResponseDto<UserResponseDto> expectedResponse = new PageResponseDto<>();
        expectedResponse.setContent(List.of(new UserResponseDto()));
        expectedResponse.setPage(0);
        expectedResponse.setSize(2);
        expectedResponse.setTotalElements(1);
        expectedResponse.setTotalPages(1);
        expectedResponse.setLast(true);

        int page = 0;
        int size = 2;
        String sort = null;
        when(userService.getPaginatedUsers(any(PageRequestDto.class))).thenReturn(expectedResponse);

        PageResponseDto<UserResponseDto> response = userController.getUsers(page, size, sort);

        ArgumentCaptor<PageRequestDto> pageRequestCaptor = ArgumentCaptor.forClass(PageRequestDto.class);

        verify(userService).getPaginatedUsers(pageRequestCaptor.capture());

        PageRequestDto capturedRequest = pageRequestCaptor.getValue();

        assertAll(
                () -> assertEquals(1, response.getContent().size()),
                () -> assertEquals(0, response.getPage()),
                () -> assertEquals(2, response.getSize()),
                () -> assertEquals(1, response.getTotalElements()),
                () -> assertEquals(1, response.getTotalPages()),
                () -> assertTrue(response.isLast()),
                () -> assertEquals(page, capturedRequest.getPage()),
                () -> assertEquals(size, capturedRequest.getSize()),
                () -> assertEquals(sort, capturedRequest.getSort())
        );
    }

    @Test
    void getUsers_WhenNoRolesExist_ShouldReturnEmptyPaginatedResponse() {
        PageResponseDto<UserResponseDto> expectedResponse = new PageResponseDto<>();
        expectedResponse.setContent(List.of());
        expectedResponse.setPage(0);
        expectedResponse.setSize(2);
        expectedResponse.setTotalElements(0);
        expectedResponse.setTotalPages(0);
        expectedResponse.setLast(true);

        int page = 0;
        int size = 2;
        String sort = null;

        when(userService.getPaginatedUsers(any(PageRequestDto.class))).thenReturn(expectedResponse);

        PageResponseDto<RolesResponseDto> response = userController.getUsers(page, size, sort);

        ArgumentCaptor<PageRequestDto> pageRequestCaptor = ArgumentCaptor.forClass(PageRequestDto.class);

        verify(userService).getPaginatedUsers(pageRequestCaptor.capture());

        PageRequestDto capturedRequest = pageRequestCaptor.getValue();

        assertAll(
                () -> assertEquals(0, response.getContent().size()),
                () -> assertEquals(0, response.getTotalElements()),
                () -> assertEquals(0, response.getTotalPages()),
                () -> assertTrue(response.isLast()),
                () -> assertEquals(page, capturedRequest.getPage()),
                () -> assertEquals(size, capturedRequest.getSize()),
                () -> assertEquals(sort, capturedRequest.getSort())
        );
    }
}