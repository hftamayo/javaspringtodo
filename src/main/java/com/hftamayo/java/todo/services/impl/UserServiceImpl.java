package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dto.RegisterUserResponseDto;
import com.hftamayo.java.todo.model.Roles;
import com.hftamayo.java.todo.model.User;
import com.hftamayo.java.todo.repository.UserRepository;
import com.hftamayo.java.todo.services.RolesService;
import com.hftamayo.java.todo.services.UserService;
import com.hftamayo.java.todo.exceptions.EntityAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolesService roleService;

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
            throw new EntityAlreadyExistsException("The email is already registered by this user: " +
                    requestedUser.get().getEmail() + " with the name: " + requestedUser.get().getName());
        }
    }

    @Override
    public RegisterUserResponseDto userToDto(User user) {
        return new RegisterUserResponseDto(user.getId(),
                user.getName(), user.getEmail(),
                user.getAge(), user.isAdmin(), user.isStatus());
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
            throw new EntityNotFoundException("User not found");
        }
    }

    @Transactional
    @Override
    public void deleteUser(long userId) {
        Optional<User> requestedUserOptional = getUserById(userId);
        if (requestedUserOptional.isPresent()) {
            userRepository.deleteById(requestedUserOptional.get().getId());
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }

    @Transactional
    @Override
    public User updateUserStatus(long userId, boolean status) {
        Optional<User> requestedUserOptional = getUserById(userId);
        if (requestedUserOptional.isPresent()) {
            User requestedUser = requestedUserOptional.get();
            requestedUser.setStatus(status);
            return userRepository.save(requestedUser);
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }

    @Transactional
    @Override
    public User updateUserStatusAndRole(long userId, boolean status, String roleEnum) {
        Optional<User> requestedUserOptional = getUserById(userId);
        if (!requestedUserOptional.isPresent()) {
            throw new EntityNotFoundException("User not found");
        }
        User user = requestedUserOptional.get();
        user.setStatus(status);

        Optional<Roles> roleOptional = roleService.getRoleByEnum(roleEnum);
        if (!roleOptional.isPresent()) {
            throw new EntityNotFoundException("Role not found");
        }
        user.getRoles().add(roleOptional.get());

        return userRepository.save(user);
    }
}
