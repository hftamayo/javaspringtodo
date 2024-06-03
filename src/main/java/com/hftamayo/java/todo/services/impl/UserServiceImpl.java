package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.model.Roles;
import com.hftamayo.java.todo.model.User;
import com.hftamayo.java.todo.repository.UserRepository;
import com.hftamayo.java.todo.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public List<User> getAllUsersByStatus(boolean isActive) {
        return userRepository.findAllByStatus(isActive);
    }

    public Optional<User> getUserById(long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByName(username);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        return userRepository.findByNameAndPassword(username, password);
    }

    public User getUserByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    public long countAllUserByUsername(String username) {
        return userRepository.countAllByName(username);
    }

    public long countAllUserByEmail(String email) {
        return userRepository.countAllByEmail(email);
    }

    @Transactional
    @Override
    public User saveUser(User newUser) {
        Optional<User> requestedUser = userRepository.findByEmail(newUser.getEmail());
        if (!requestedUser.isPresent()) {
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            return userRepository.save(newUser);
        } else {
            throw new RuntimeException("The email is already registered by this user: " +
                    requestedUser.get().getEmail() + " with the name: " + requestedUser.get().getName());
        }
    }

    @Transactional
    @Override
    public User updateUser(long userId, User updatedUser) {
        Optional<User> requestedUserOptional = getUserById(userId);
        if (requestedUserOptional.isPresent()) {
            User requestedUser = requestedUserOptional.get();
            requestedUser.setName(updatedUser.getName());
            requestedUser.setEmail(updatedUser.getEmail());
            requestedUser.setPassword(updatedUser.getPassword());
            requestedUser.setAge(updatedUser.getAge());
            requestedUser.setAdmin(updatedUser.isAdmin());
            requestedUser.setStatus(updatedUser.isStatus());
            return userRepository.save(requestedUser);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Transactional
    @Override
    public void deleteUser(long userId) {
        boolean userExists = userRepository.existsById(userId);
        if (userExists) {
            userRepository.deleteById(userId);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Transactional
    @Override
    public User updateUserStatus(long userId, boolean status) {
        User requestedUser = userRepository.findById(userId).get();
        if (requestedUser != null) {
            requestedUser.setStatus(status);
            return userRepository.save(requestedUser);
        } else {
            throw new RuntimeException("User not found");
        }


    }

}
