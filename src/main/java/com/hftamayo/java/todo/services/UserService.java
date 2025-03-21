package com.hftamayo.java.todo.services;

import com.hftamayo.java.todo.dto.CrudOperationResponseDto;
import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> loginRequest(String email);

    CrudOperationResponseDto<UserResponseDto> getUsers();

    CrudOperationResponseDto<UserResponseDto> getUser(long userId);

    CrudOperationResponseDto<UserResponseDto> getUserByCriteria(String criteria, String value);

    CrudOperationResponseDto<UserResponseDto> getUserByCriterias(String criteria, String value,
                                                                 String criteria2, String value2);

    CrudOperationResponseDto<UserResponseDto> saveUser(User newUser);

    CrudOperationResponseDto<UserResponseDto> updateUser(long userId, User updatedUser);

    CrudOperationResponseDto<UserResponseDto> updateUserStatus(long userId, boolean status);

    CrudOperationResponseDto<UserResponseDto> updateUserStatusAndRole(long userId, boolean status, String roleEnum);

    CrudOperationResponseDto deleteUser(long userId);

}
