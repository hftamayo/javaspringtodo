package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.EndpointResponseDto;
import com.hftamayo.java.todo.dto.pagination.PageRequestDto;
import com.hftamayo.java.todo.dto.pagination.PaginatedDataDto;
import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.entity.User;
import com.hftamayo.java.todo.exceptions.ResourceNotFoundException;
import com.hftamayo.java.todo.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

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
        // Arrange
        int page = 0;
        int size = 2;
        String sort = null;

        PaginatedDataDto<UserResponseDto> paginatedData = getUsersResponseDtoPaginatedDataDto();

        when(userService.getPaginatedUsers(any(PageRequestDto.class))).thenReturn(paginatedData);

        // Act
        ResponseEntity<EndpointResponseDto<?>> response = userController.getUsers(page, size, sort);

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("OPERATION_SUCCESS", response.getBody().getResultMessage()),
                () -> assertEquals(200, response.getBody().getCode()),
                () -> assertNotNull(response.getBody().getData()),
                () -> verify(userService).getPaginatedUsers(any(PageRequestDto.class))
        );
    }

    @Test
    void getUser_WhenUserExists_ShouldReturnSuccessResponse() {
        // Arrange
        long userId = 1L;
        UserResponseDto userResponse = new UserResponseDto();
        userResponse.setId(userId);
        userResponse.setEmail("test@example.com");

        when(userService.getUser(userId)).thenReturn(userResponse);

        // Act
        ResponseEntity<EndpointResponseDto<?>> response = userController.getUser(userId);

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("OPERATION_SUCCESS", response.getBody().getResultMessage()),
                () -> assertEquals(200, response.getBody().getCode()),
                () -> assertNotNull(response.getBody().getData()),
                () -> verify(userService).getUser(userId)
        );
    }

    @Test
    void getUser_WhenUserNotFound_ShouldReturnNotFoundResponse() {
        // Arrange
        long userId = 999L;
        when(userService.getUser(userId)).thenThrow(new ResourceNotFoundException("User not found"));

        // Act
        ResponseEntity<EndpointResponseDto<?>> response = userController.getUser(userId);

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("User not found", response.getBody().getResultMessage()),
                () -> assertEquals(404, response.getBody().getCode()),
                () -> verify(userService).getUser(userId)
        );
    }

    @Test
    void getUsers_WhenNoUsersExist_ShouldReturnEmptyPaginatedResponse() {
        // Arrange
        int page = 0;
        int size = 2;
        String sort = null;

        PaginatedDataDto<UserResponseDto> emptyPaginatedData = new PaginatedDataDto<>();
        emptyPaginatedData.setContent(List.of());
        emptyPaginatedData.setTotalElements(0L);
        emptyPaginatedData.setTotalPages(0);
        emptyPaginatedData.setSize(2);
        emptyPaginatedData.setNumber(0);
        emptyPaginatedData.setFirst(true);
        emptyPaginatedData.setLast(true);
        emptyPaginatedData.setNumberOfElements(0);

        when(userService.getPaginatedUsers(any(PageRequestDto.class))).thenReturn(emptyPaginatedData);

        // Act
        ResponseEntity<EndpointResponseDto<?>> response = userController.getUsers(page, size, sort);

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("OPERATION_SUCCESS", response.getBody().getResultMessage()),
                () -> assertEquals(200, response.getBody().getCode()),
                () -> assertNotNull(response.getBody().getData()),
                () -> verify(userService).getPaginatedUsers(any(PageRequestDto.class))
        );
    }

    // Helper method
    private PaginatedDataDto<UserResponseDto> getUsersResponseDtoPaginatedDataDto() {
        UserResponseDto userResponse = new UserResponseDto();
        userResponse.setId(1L);
        userResponse.setEmail("test@example.com");

        PaginatedDataDto<UserResponseDto> paginatedData = new PaginatedDataDto<>();
        paginatedData.setContent(List.of(userResponse));
        paginatedData.setTotalElements(1L);
        paginatedData.setTotalPages(1);
        paginatedData.setSize(2);
        paginatedData.setNumber(0);
        paginatedData.setFirst(true);
        paginatedData.setLast(true);
        paginatedData.setNumberOfElements(1);

        return paginatedData;
    }
}