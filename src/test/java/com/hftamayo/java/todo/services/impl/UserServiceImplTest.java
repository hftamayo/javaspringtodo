package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dto.CrudOperationResponseDto;
import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.entity.User;
import com.hftamayo.java.todo.mapper.UserMapper;
import com.hftamayo.java.todo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getUsers_ShouldReturnListOfUsers() {
        List<User> usersList = List.of(
                createUser(1L, "John Doe"),
                createUser(2L, "Jane Doe")
        );
        List<UserResponseDto> responseDtos = List.of(
                createUserDto(1L, "John Doe"),
                createUserDto(2L, "Jane Doe")
        );

        when(userRepository.findAll()).thenReturn(usersList);
        when(userMapper.toUserResponseDto(any(User.class)))
                .thenReturn(responseDtos.get(0), responseDtos.get(1));

        CrudOperationResponseDto<UserResponseDto> result = userService.getUsers();

        assertEquals(200, result.getCode());
        assertEquals("OPERATION SUCCESSFUL", result.getResultMessage());
        assertEquals(2, ((List<UserResponseDto>)result.getData()).size());
        verify(userRepository).findAll();
        verify(userMapper, times(2)).toUserResponseDto(any(User.class));
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnSuccessResponse() {
        Long userId = 1L;
        User user = createUser(userId, "John Doe");
        UserResponseDto responseDto = createUserDto(userId, "John Doe");

        when(userRepository.findUserById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toUserResponseDto(user)).thenReturn(responseDto);

        CrudOperationResponseDto<UserResponseDto> result = userService.getUser(userId);

        assertEquals(200, result.getCode());
        assertEquals("OPERATION SUCCESSFUL", result.getResultMessage());
        assertEquals(responseDto, result.getData());
        verify(userRepository).findById(userId);
        verify(userMapper).toUserResponseDto(user);
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldReturnNotFoundResponse() {
        Long userId = 1L;

        when(userRepository.findUserById(userId)).thenReturn(Optional.empty());

        CrudOperationResponseDto<UserResponseDto> result = userService.getUser(userId);

        assertEquals(404, result.getCode());
        assertEquals("USER NOT FOUND", result.getResultMessage());
        assertNull(result.getData());
        verify(userRepository).findById(userId);
    }

    @Test
    void saveUser_WhenUserIsValid_ShouldReturnSuccessResponse() {
        User newUser = createUser(null, "John Doe");
        User savedUser = createUser(1L, "John Doe");
        UserResponseDto responseDto = createUserDto(1L, "John Doe");

        when(userRepository.save(newUser)).thenReturn(savedUser);
        when(userMapper.toUserResponseDto(savedUser)).thenReturn(responseDto);

        CrudOperationResponseDto<UserResponseDto> result = userService.saveUser(newUser);

        assertEquals(201, result.getCode());
        assertEquals("USER CREATED SUCCESSFULLY", result.getResultMessage());
        assertEquals(responseDto, result.getData());
        verify(userRepository).save(newUser);
        verify(userMapper).toUserResponseDto(savedUser);
    }

    @Test
    void saveUser_WhenUserAlreadyExists_ShouldReturnConflictResponse() {
        User existingUser = createUser(1L, "John Doe");

        when(userRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));

        CrudOperationResponseDto<UserResponseDto> result = userService.saveUser(existingUser);

        assertEquals(401, result.getCode());
        assertEquals("USER ALREADY EXISTS", result.getResultMessage());
        assertNull(result.getData());
        verify(userRepository).findById(existingUser.getId());
    }

    @Test
    void updateUser_WhenUserExists_ShouldReturnSuccessResponse() {
        Long userId = 1L;
        User existingUser = createUser(userId, "John Doe");
        User updatedUser = createUser(userId, "John Smith");
        UserResponseDto responseDto = createUserDto(userId, "John Smith");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        when(userMapper.toUserResponseDto(updatedUser)).thenReturn(responseDto);

        CrudOperationResponseDto<UserResponseDto> result = userService.updateUser(userId, updatedUser);

        assertEquals(200, result.getCode());
        assertEquals("USER UPDATED SUCCESSFULLY", result.getResultMessage());
        assertEquals(responseDto, result.getData());
        verify(userRepository).findById(userId);
        verify(userRepository).save(updatedUser);
        verify(userMapper).toUserResponseDto(updatedUser);
    }

    @Test
    void updateUser_WhenUserDoesNotExist_ShouldReturnNotFoundResponse() {
        Long userId = 1L;
        User updatedUser = createUser(userId, "John Smith");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        CrudOperationResponseDto<UserResponseDto> result = userService.updateUser(userId, updatedUser);

        assertEquals(404, result.getCode());
        assertEquals("USER NOT FOUND", result.getResultMessage());
        assertNull(result.getData());
        verify(userRepository).findById(userId);
    }

    @Test
    void deleteUser_WhenUserExists_ShouldReturnSuccessResponse() {
        Long userId = 1L;
        User existingUser = createUser(userId, "John Doe");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        CrudOperationResponseDto result = userService.deleteUser(userId);

        assertEquals(200, result.getCode());
        assertEquals("USER DELETED SUCCESSFULLY", result.getResultMessage());
        verify(userRepository).findById(userId);
        verify(userRepository).delete(existingUser);
    }

    @Test
    void deleteUser_WhenUserDoesNotExist_ShouldReturnNotFoundResponse() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        CrudOperationResponseDto result = userService.deleteUser(userId);

        assertEquals(404, result.getCode());
        assertEquals("USER NOT FOUND", result.getResultMessage());
        assertNull(result.getData());
        verify(userRepository).findById(userId);
    }

    @Test
    void deleteUser_WhenUserIsNotActive_ShouldReturnConflictResponse() {
        Long userId = 1L;
        User existingUser = createUser(userId, "John Doe");
        existingUser.setStatus(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        CrudOperationResponseDto result = userService.deleteUser(userId);

        assertEquals(401, result.getCode());
        assertEquals("USER IS NOT ACTIVE", result.getResultMessage());
        assertNull(result.getData());
        verify(userRepository).findById(userId);
    }

    @Test
    void deleteUser_WhenUserIsNotFound_ShouldReturnNotFoundResponse() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        CrudOperationResponseDto result = userService.deleteUser(userId);

        assertEquals(404, result.getCode());
        assertEquals("USER NOT FOUND", result.getResultMessage());
        assertNull(result.getData());
        verify(userRepository).findById(userId);
    }

    private User createUser(Long id, String name) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setStatus(true);
        return user;
    }

    private UserResponseDto createUserDto(Long id, String name) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(id);
        dto.setName(name);
        dto.setStatus(true);
        return dto;
    }



}