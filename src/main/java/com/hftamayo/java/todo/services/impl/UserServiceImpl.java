package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dto.CrudOperationResponseDto;
import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.entity.ERole;
import com.hftamayo.java.todo.entity.Roles;
import com.hftamayo.java.todo.entity.User;
import com.hftamayo.java.todo.repository.RolesRepository;
import com.hftamayo.java.todo.repository.UserRepository;
import com.hftamayo.java.todo.services.UserService;
import com.hftamayo.java.todo.exceptions.EntityAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
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
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponseDto> getUsers() {
        List<User> usersList = userRepository.findAll();
        return usersList.stream().map(this::userToDto).toList();
    }

    @Override
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

    @Override
    public Optional<User> getUserById(long userId) {
        return userRepository.findUserById(userId);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public Optional<List<UserResponseDto>> getUserByCriteria(String criteria, String value) {
        Specification<User> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(criteria), value);

        List<User> usersList = userRepository.findAll(specification);
        if (!usersList.isEmpty()) {
            return Optional.of(userListToDto(usersList));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<UserResponseDto>> getUserByCriterias(String criteria, String value,
                                                              String criteria2, String value2) {
        Specification<User> specification = (root, query, criteriaBuilder)
                -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get(criteria), value),
                criteriaBuilder.equal(root.get(criteria2), value2)
        );

        List<User> usersList = userRepository.findAll(specification);
        if (!usersList.isEmpty()) {
            return Optional.of(userListToDto(usersList));
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public CrudOperationResponseDto<UserResponseDto> saveUser(User newUser) {
        Optional<User> requestedUser = getUserByEmail(newUser.getEmail());
        if (!requestedUser.isPresent()) {
            if (newUser.getRole() == null) {
                Roles defaultRole = rolesRepository.findByRoleEnum(ERole.ROLE_USER)
                        .orElseThrow(() -> new EntityNotFoundException("Role not found"));
                newUser.setRole(defaultRole);
            }
            String encodedPassword = passwordEncoder.encode(newUser.getPassword().trim());
            newUser.setPassword(encodedPassword);
            User savedUser = userRepository.save(newUser);
            UserResponseDto dtoObject = userToDto(savedUser);
            CrudOperationResponseDto<UserResponseDto> response = new CrudOperationResponseDto<>(200, "OPERATION SUCCESSFUL", dtoObject);
            return response;
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
            User existingUser = requestedUserOptional.get();
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setAge(updatedUser.getAge());
            existingUser.setAdmin(updatedUser.isAdmin());
            existingUser.setStatus(updatedUser.isStatus());
            User savedUser = userRepository.save(existingUser);
            return userToDto(savedUser);
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }

    @Transactional
    @Override
    public UserResponseDto updateUserStatus(long userId, boolean status) {
        Optional<User> requestedUserOptional = getUserById(userId);
        if (requestedUserOptional.isPresent()) {
            User existingUser = requestedUserOptional.get();
            existingUser.setStatus(status);
            User savedUser = userRepository.save(existingUser);
            return userToDto(savedUser);
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

        Optional<Roles> roleOptional = rolesRepository.findByRoleEnum(ERole.valueOf(roleEnum));
        if (!roleOptional.isPresent()) {
            throw new EntityNotFoundException("Role not found");
        }
        user.setRole(roleOptional.get());
        User savedUser = userRepository.save(user);
        return userToDto(savedUser);
    }

    @Transactional
    @Override
    public CrudOperationResponseDto deleteUser(long userId) {
        Optional<User> requestedUserOptional = getUserById(userId);
        if (requestedUserOptional.isPresent()) {
            userRepository.deleteUserById(requestedUserOptional.get().getId());
            return new CrudOperationResponseDto(200, "OPERATION SUCCESSFUL");
        } else {
            return new CrudOperationResponseDto(404, "USER NOT FOUND");
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