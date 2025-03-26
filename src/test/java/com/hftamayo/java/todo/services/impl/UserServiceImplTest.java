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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
        // Arrange
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

        // Act
        CrudOperationResponseDto<List<UserResponseDto>> result = userService.getUsers();

        // Assert
        assertEquals(200, result.getCode());
        assertEquals("OPERATION SUCCESSFUL", result.getResultMessage());
        assertEquals(2, result.getData().size());
        verify(userRepository).findAll();
        verify(userMapper, times(2)).toUserResponseDto(any(User.class));
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