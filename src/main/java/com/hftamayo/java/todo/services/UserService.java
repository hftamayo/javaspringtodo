package com.hftamayo.java.todo.services;

import com.hftamayo.java.todo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getUsers();
    List<User> getAllUsersByStatus(boolean isActive);
    User getUserById(long userId);
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserByEmail(String email);
    User getUserByUsernameAndPassword(String username, String password);
    User getUserByEmailAndPassword(String email, String password);
    long countAllUserByUsername(String username);
    long countAllUserByEmail(String email);

    User saveUser(User newUser);
    User updateUser(long userId, User updatedUser);
    void deleteUser(long userId);
}
