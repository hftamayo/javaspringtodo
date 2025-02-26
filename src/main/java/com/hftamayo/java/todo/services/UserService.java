package com.hftamayo.java.todo.services;

import com.hftamayo.java.todo.dto.CrudOperationResponseDto;
import com.hftamayo.java.todo.dto.user.UserOperationResponseDto;
import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserResponseDto> getUsers();

    Optional<UserResponseDto> getUser(long userId);

    Optional<User> getUserById(long userId);

    Optional<User> getUserByEmail(String userEmail);

    Optional<List<UserResponseDto>> getUserByCriteria(String criteria, String value);

    Optional<List<UserResponseDto>> getUserByCriterias(String criteria, String value,
                                                       String criteria2, String value2);

    UserResponseDto saveUser(User newUser);

    UserResponseDto updateUser(long userId, User updatedUser);

    CrudOperationResponseDto deleteUser(long userId);

    UserResponseDto updateUserStatus(long userId, boolean status);

    UserResponseDto updateUserStatusAndRole(long userId, boolean status, String roleEnum);

    UserResponseDto userToDto(User user);
}
