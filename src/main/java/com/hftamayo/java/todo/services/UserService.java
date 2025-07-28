package com.hftamayo.java.todo.services;

import com.hftamayo.java.todo.dto.pagination.PageRequestDto;
import com.hftamayo.java.todo.dto.pagination.PaginatedDataDto;
import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> loginRequest(String email);

    List<UserResponseDto> getUsers();

    UserResponseDto getUser(long userId);

    UserResponseDto getUserByCriteria(String criteria, String value);

    UserResponseDto getUserByCriterias(String criteria, String value,
                                       String criteria2, String value2);

    PaginatedDataDto<UserResponseDto> getPaginatedUsers(PageRequestDto pageRequestDto);

    UserResponseDto saveUser(User newUser);

    UserResponseDto updateUser(long userId, User updatedUser);

    UserResponseDto updateUserStatus(long userId, boolean status);

    UserResponseDto updateUserStatusAndRole(long userId, boolean status, String roleEnum);

    void deleteUser(long userId);
}
