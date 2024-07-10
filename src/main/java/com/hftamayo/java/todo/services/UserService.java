package com.hftamayo.java.todo.services;

import com.hftamayo.java.todo.dto.auth.RegisterUserResponseDto;
import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserResponseDto> getUsers();

    Optional<User> getUserById(long userId);

    Optional<UserResponseDto> getUser(long userId);

    Optional<UserResponseDto> getUserByCriteria(String criteria, String value);



    List<User> getUsersByStatus(boolean isActive);

    Optional<User> getUserByEmail(String userEmail);

    Optional<User> getUserByName(String userName);

    Optional<User> getUserByNameAndPassword(String userName, String userPassword);

    Optional<User> getUserByEmailAndPassword(String userEmail, String userPassword);

    long countAllByCriteria(String criteria, String value);

    User saveUser(User newUser);

    UserResponseDto updateUser(long userId, User updatedUser);

    void deleteUser(long userId);

    User updateUserStatus(long userId, boolean status);

    User updateUserStatusAndRole(long userId, boolean status, String roleEnum);

    RegisterUserResponseDto userToDto(User user);

    UserResponseDto usersToDto(User user);


}
