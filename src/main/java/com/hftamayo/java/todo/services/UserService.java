package com.hftamayo.java.todo.services;

import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserResponseDto> getUsers();

    Optional<UserResponseDto> getUser(long userId);

    Optional<User> getUserById(long userId);

    Optional<User> getUserByEmail(String userEmail);

    Optional<List<UserResponseDto>> getUserByCriteria(String criteria, String value);

    Optional<List<UserResponseDto>> getUserByCriterias(String criteria, String value, String criteria2, String value2);

    UserResponseDto saveUser(User newUser);

    UserResponseDto updateUser(long userId, User updatedUser);

    void deleteUser(long userId);

    User updateUserStatus(long userId, boolean status);

    UserResponseDto updateUserStatusAndRole(long userId, boolean status, String roleEnum);

    UserResponseDto usersToDto(User user);


}
