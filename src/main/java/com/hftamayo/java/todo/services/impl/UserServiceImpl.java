package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dao.UserDao;
import com.hftamayo.java.todo.dao.RolesDao;
import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.model.Roles;
import com.hftamayo.java.todo.model.User;
import com.hftamayo.java.todo.services.UserService;
import com.hftamayo.java.todo.exceptions.EntityAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    private final @Lazy PasswordEncoder passwordEncoder;
    private final RolesDao rolesDao;

    public List<UserResponseDto> getUsers() {
        List<User> usersList = userDao.getUsers();
        return usersList.stream().map(this::userToDto).toList();
    }

    public Optional<UserResponseDto> getUser(long userId) {
        Optional<User> userOptional = getUserById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserResponseDto dto = userToDto(user);
            return Optional.of(dto);
        } else {
            return Optional.empty();
        }
    }

    public Optional<User> getUserById(long userId) {
        return userDao.getUserById(userId);
    }

    public Optional<User> getUserByEmail(String email) {
        Optional<Object> result = userDao.getUserByCriteria("email", email, true);
        return result.map(object -> (User) object);
    }

    public Optional<List<UserResponseDto>> getUserByCriteria(String criteria, String value) {
        Optional<Object> result = userDao.getUserByCriteria(criteria, value, false);
        return result.map(object -> {
            if (object instanceof List<?> &&
                    !((List<?>) object).isEmpty() && ((List<?>) object).get(0) instanceof User) {
                List<User> usersList = (List<User>) object;
                return Optional.of(userListToDto(usersList));
            }
            return Optional.<List<UserResponseDto>>empty();
        }).orElse(Optional.empty());
    }

    public Optional<List<UserResponseDto>> getUserByCriterias(
            String criteria, String value, String criteria2, String value2) {
        Optional<List<User>> userListOptional = userDao.getUserByCriterias(criteria, value, criteria2, value2);
        return userListOptional.map(this::userListToDto).map(Optional::of).orElse(Optional.empty());
    }

    @Transactional
    @Override
    public UserResponseDto saveUser(User newUser) {
        Optional<User> requestedUser = getUserByEmail(newUser.getEmail());
        if (!requestedUser.isPresent()) {
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            User savedUser = userDao.saveUser(newUser);
            UserResponseDto dto = userToDto(savedUser);
            return dto;
        } else {
            throw new EntityAlreadyExistsException("The email is already registered by this user: " +
                    requestedUser.get().getEmail() + " with the name: " + requestedUser.get().getName());
        }
    }

    @Transactional
    @Override
    public UserResponseDto updateUser(long userId, User updatedUser) {
        Optional<User> requestedUserOptional = getUserById(userId);
        if (requestedUserOptional.isPresent()) {
            Map<String, Object> propertiesToUpdate = new HashMap<>();
            propertiesToUpdate.put("name", updatedUser.getName());
            propertiesToUpdate.put("email", updatedUser.getEmail());
            propertiesToUpdate.put("password", updatedUser.getPassword());
            propertiesToUpdate.put("age", updatedUser.getAge());
            propertiesToUpdate.put("isAdmin", updatedUser.isAdmin());
            propertiesToUpdate.put("status", updatedUser.isStatus());
            User user = userDao.updateUser(userId, propertiesToUpdate);
            return userToDto(user);
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }

    @Transactional
    @Override
    public UserResponseDto updateUserStatus(long userId, boolean status) {
        Optional<User> requestedUserOptional = getUserById(userId);
        if (requestedUserOptional.isPresent()) {
            Map<String, Object> propertiesToUpdate = new HashMap<>();
            propertiesToUpdate.put("status", status);
            User user = userDao.updateUser(userId, propertiesToUpdate);
            return userToDto(user);
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }

    @Transactional
    @Override
    public UserResponseDto updateUserStatusAndRole(long userId, boolean status, String roleEnum) {
        Optional<User> requestedUserOptional = getUserById(userId);
        if (!requestedUserOptional.isPresent()) {
            throw new EntityNotFoundException("User not found");
        }
        User user = requestedUserOptional.get();
        user.setStatus(status);

        Optional<Roles> roleOptional = rolesDao.getRoleByEnum(roleEnum);
        if (!roleOptional.isPresent()) {
            throw new EntityNotFoundException("Role not found");
        }
        Map<String, Object> propertiesToUpdate = new HashMap<>();
        propertiesToUpdate.put("status", status);
        propertiesToUpdate.put("role", roleOptional.get());
        User updatedUser = userDao.updateUser(userId, propertiesToUpdate);
        return userToDto(updatedUser);
    }

    @Transactional
    @Override
    public void deleteUser(long userId) {
        Optional<User> requestedUserOptional = getUserById(userId);
        if (requestedUserOptional.isPresent()) {
            userDao.deleteUser(requestedUserOptional.get().getId());
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }

    @Override
    public UserResponseDto userToDto(User user) {
        String formattedRoleName = user.getRole().getRoleEnum().name();

        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.isAdmin(),
                user.isAccountNonExpired(),
                user.isAccountNonLocked(),
                user.isCredentialsNonExpired(),
                user.isStatus(),
                user.getDateAdded().toString(),
                formattedRoleName
        );
    }

    private List<UserResponseDto> userListToDto(List<User> users) {
        return users.stream()
                .map(this::userToDto)
                .collect(Collectors.toList());
    }
}