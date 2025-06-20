package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.CrudOperationResponseDto;
import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.entity.User;
import com.hftamayo.java.todo.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    void deleteUser_WhenUserExists_ShouldReturnSuccessResponse() {
        long userId = 1L;
        CrudOperationResponseDto expectedResponse = new CrudOperationResponseDto<>();
        expectedResponse.setCode(200);
        expectedResponse.setResultMessage("OPERATION SUCCESSFUL");

        when(userService.deleteUser(userId)).thenReturn(expectedResponse);

        CrudOperationResponseDto response = userController.deleteUser(userId);

        assertAll(
                () -> assertEquals(200, response.getCode()),
                () -> assertEquals("OPERATION SUCCESSFUL", response.getResultMessage()),
                () -> verify(userService).deleteUser(userId)
        );
    }
}