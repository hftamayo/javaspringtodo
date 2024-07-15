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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    private final PasswordEncoder passwordEncoder;
    private final RolesDao rolesDao;

    public List<UserResponseDto> getUsers() {
        List<User> usersList = userDao.getUsers();
        return usersList.stream().map(this::usersToDto).toList();
    }

    public Optional<UserResponseDto> getUser(long userId) {
        Optional<User> userOptional = getUserById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserResponseDto dto = usersToDto(user);
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
        if (result.isPresent() && result.get() instanceof List<?>) {
            List<?> rawList = (List<?>) result.get();
            if (!rawList.isEmpty() && rawList.get(0) instanceof User) {
                List<User> usersList = (List<User>) rawList;
                return Optional.of(usersList.stream().map(this::usersToDto).collect(Collectors.toList()));
            }
        }
        return Optional.empty();
    }

    public Optional<List<UserResponseDto>> getUserByCriterias(String criteria, String value, String criteria2, String value2) {
        Optional<List<User>> userListOptional = userDao.getUserByCriterias(criteria, value, criteria2, value2);
        return userListOptional.map(usersList -> {
            int listSize = usersList.size();
            return usersList.stream()
                    .map(user -> {
                        UserResponseDto dto = usersToDto(user);
                        dto.setListSize(listSize); // Set the list size for each DTO
                        return dto;
                    })
                    .collect(Collectors.toList());
        });
    }

    @Transactional
    @Override
    public UserResponseDto saveUser(User newUser) {
        Optional<User> requestedUser = getUserByEmail(newUser.getEmail());
        if (!requestedUser.isPresent()) {
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            User savedUser = userDao.saveUser(newUser);
            UserResponseDto dto = usersToDto(savedUser);
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
            User requestedUser = requestedUserOptional.get();
            requestedUser.setName(updatedUser.getName());
            requestedUser.setEmail(updatedUser.getEmail());
            requestedUser.setPassword(updatedUser.getPassword());
            requestedUser.setAge(updatedUser.getAge());
            requestedUser.setAdmin(updatedUser.isAdmin());
            requestedUser.setStatus(updatedUser.isStatus());
            User userToUpdate = userDao.updateUser(userId, requestedUser);
            UserResponseDto dto = usersToDto(userToUpdate);
            return dto;
        } else {
            throw new EntityNotFoundException("User not found");
        }
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

    @Transactional
    @Override
    public User updateUserStatus(long userId, boolean status) {
        Optional<User> requestedUserOptional = getUserById(userId);
        if (requestedUserOptional.isPresent()) {
            User requestedUser = requestedUserOptional.get();
            requestedUser.setStatus(status);
            return userDao.updateUser(userId, requestedUser);
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
        user.setRole(roleOptional.get());
        User updatedUser = userDao.updateUser(userId, user);
        return usersToDto(updatedUser);
    }

    @Override
    public UserResponseDto usersToDto(User user) {
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

}
