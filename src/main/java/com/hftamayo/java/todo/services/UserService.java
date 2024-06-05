package com.hftamayo.java.todo.services;

import com.hftamayo.java.todo.dto.RegisterUserResponseDto;
import com.hftamayo.java.todo.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getUsers();
    List<User> getAllUsersByStatus(boolean isActive);
    Optional<User> getUserById(long userId);
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserByEmail(String email);
    User getUserByUsernameAndPassword(String username, String password);
    User getUserByEmailAndPassword(String email, String password);
    long countAllUserByUsername(String username);
    long countAllUserByEmail(String email);

    User saveUser(User newUser);
    RegisterUserResponseDto userToDto(User user);
    User updateUser(long userId, User updatedUser);
    User updateUserStatus(long userId, boolean status);
    void deleteUser(long userId);

    User updateUserStatusAndRole(long userId, boolean status, String roleEnum);


}
