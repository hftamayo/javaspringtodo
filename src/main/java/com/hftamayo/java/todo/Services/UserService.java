package com.hftamayo.java.todo.Services;

import com.hftamayo.java.todo.Model.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();
    List<User> getAllUsersByStatus(boolean isActive);
    User getUserById(long userId);
    User getUserByUsername(String username);
    User getUserByEmail(String email);
    User getUserByUsernameAndPassword(String username, String password);
    User getUserByEmailAndPassword(String email, String password);
    long countAllUserByUsername(String username);
    long countAllUserByEmail(String email);

    User saveUser(User newUser);
    User updateUser(long userId, User updatedUser);
    void deleteUser(long userId);
}
