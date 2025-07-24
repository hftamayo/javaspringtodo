package com.hftamayo.java.todo.services;

import com.hftamayo.java.todo.dto.EndpointResponseDto;
import com.hftamayo.java.todo.dto.pagination.PageRequestDto;
import com.hftamayo.java.todo.dto.pagination.PaginatedDataDto;
import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> loginRequest(String email);

    EndpointResponseDto<UserResponseDto> getUsers();

    EndpointResponseDto<UserResponseDto> getUser(long userId);

    EndpointResponseDto<UserResponseDto> getUserByCriteria(String criteria, String value);

    EndpointResponseDto<UserResponseDto> getUserByCriterias(String criteria, String value,
                                                                 String criteria2, String value2);

    PaginatedDataDto<UserResponseDto> getPaginatedUsers(PageRequestDto pageRequestDto);

    EndpointResponseDto<UserResponseDto> saveUser(User newUser);

    EndpointResponseDto<UserResponseDto> updateUser(long userId, User updatedUser);

    EndpointResponseDto<UserResponseDto> updateUserStatus(long userId, boolean status);

    EndpointResponseDto<UserResponseDto> updateUserStatusAndRole(long userId, boolean status, String roleEnum);

    EndpointResponseDto deleteUser(long userId);

}
