package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dto.CrudOperationResponseDto;
import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.entity.ERole;
import com.hftamayo.java.todo.entity.Roles;
import com.hftamayo.java.todo.entity.User;
import com.hftamayo.java.todo.mapper.UserMapper;
import com.hftamayo.java.todo.repository.RolesRepository;
import com.hftamayo.java.todo.repository.UserRepository;
import com.hftamayo.java.todo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;
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
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    //helper methods

    private Optional<User> getUserById(long userId) {
        return userRepository.findUserById(userId);
    }

    private Optional<User> getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    private static @NotNull User getExistingUser(User updatedUser, Optional<User> requestedUserOptional) {
        User existingUser = requestedUserOptional.get();

        if (updatedUser.getName() != null) {
            existingUser.setName(updatedUser.getName());
        }
        if (updatedUser.getEmail() != null) {
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getAge() >= 18 && updatedUser.getAge() != existingUser.getAge()) {
            existingUser.setAge(updatedUser.getAge());
        }
        //isAdmin, isStatus, role and password have to be updated separately
        return existingUser;
    }

    @NotNull
    private CrudOperationResponseDto<UserResponseDto> searchUserByCriteria(Specification<User> specification) {
        try {
            List<User> usersList = userRepository.findAll(specification);
            if (!usersList.isEmpty()) {
                List<UserResponseDto> usersDtoList = usersList.stream().map(userMapper::userToDto).toList();
                return new CrudOperationResponseDto(200, "OPERATION SUCCESSFUL", usersDtoList);
            } else {
                return new CrudOperationResponseDto(404, "NO USERS FOUND");
            }
        } catch (Exception e) {
            return new CrudOperationResponseDto(500, "INTERNAL SERVER ERROR");
        }
    }

    //persistence methods
    @Override
    public CrudOperationResponseDto<UserResponseDto> getUsers() {
        try {
            List<User> usersList = userRepository.findAll();
            if (!usersList.isEmpty()) {
                List<UserResponseDto> usersDtoList = usersList.stream().map(userMapper::userToDto).toList();
                return new CrudOperationResponseDto(200, "OPERATION SUCCESSFUL", usersDtoList);
            } else {
                return new CrudOperationResponseDto(404, "NO USERS FOUND");
            }
        } catch (Exception e) {
            return new CrudOperationResponseDto(500, "INTERNAL SERVER ERROR");
        }
    }

    @Override
    public CrudOperationResponseDto<UserResponseDto> getUser(long userId) {
        try {
            Optional<User> userOptional = getUserById(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                UserResponseDto userToDto = userMapper.userToDto(user);
                return new CrudOperationResponseDto(200, "OPERATION SUCCESSFUL", userToDto);
            } else {
                return new CrudOperationResponseDto(404, "USER NOT FOUND");
            }
        } catch (Exception e) {
            return new CrudOperationResponseDto(500, "INTERNAL SERVER ERROR");
        }
    }

    @Override
    public CrudOperationResponseDto<UserResponseDto> getUserByCriteria(String criteria, String value) {
        try {
            Specification<User> specification = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(criteria), value);

            return searchUserByCriteria(specification);
        } catch (Exception e) {
            return new CrudOperationResponseDto(500, "INTERNAL SERVER ERROR");
        }
    }

    @Override
    public CrudOperationResponseDto<UserResponseDto> getUserByCriterias(String criteria, String value,
                                                                        String criteria2, String value2) {
        try {
            Specification<User> specification = (root, query, criteriaBuilder)
                    -> criteriaBuilder.and(
                    criteriaBuilder.equal(root.get(criteria), value),
                    criteriaBuilder.equal(root.get(criteria2), value2)
            );

            return searchUserByCriteria(specification);
        } catch (Exception e) {
            return new CrudOperationResponseDto(500, "INTERNAL SERVER ERROR");
        }
    }

    @Transactional
    @Override
    public CrudOperationResponseDto<UserResponseDto> saveUser(User newUser) {
        try {
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
                UserResponseDto dtoObject = userMapper.userToDto(savedUser);
                return new CrudOperationResponseDto(201, "OPERATION SUCCESSFUL", dtoObject);
            } else {
                return new CrudOperationResponseDto(400, "USER ALREADY EXISTS");
            }
        } catch (Exception e) {
            return new CrudOperationResponseDto(500, "INTERNAL SERVER ERROR");
        }
    }

    @Transactional
    @Override
    public CrudOperationResponseDto<UserResponseDto> updateUser(long userId, User updatedUser) {
        try {
            Optional<User> requestedUserOptional = getUserById(userId);
            if (requestedUserOptional.isPresent()) {
                User existingUser = getExistingUser(updatedUser, requestedUserOptional);

                User savedUser = userRepository.save(existingUser);
                UserResponseDto dtoObject = userMapper.userToDto(savedUser);

                return new CrudOperationResponseDto(200, "OPERATION SUCCESSFUL", dtoObject);
            } else {
                return new CrudOperationResponseDto(404, "USER NOT FOUND");
            }
        } catch (Exception e) {
            return new CrudOperationResponseDto(500, "INTERNAL SERVER ERROR");
        }
    }

    @Transactional
    @Override
    public CrudOperationResponseDto<UserResponseDto> updateUserStatus(long userId, boolean status) {
        try {
            Optional<User> requestedUserOptional = getUserById(userId);
            if (requestedUserOptional.isPresent()) {
                User existingUser = requestedUserOptional.get();
                existingUser.setStatus(status);
                User savedUser = userRepository.save(existingUser);
                UserResponseDto dtoObject = userMapper.userToDto(savedUser);
                return new CrudOperationResponseDto(200, "OPERATION SUCCESSFUL", dtoObject);
            } else {
                return new CrudOperationResponseDto(404, "USER NOT FOUND");
            }
        } catch (Exception e) {
            return new CrudOperationResponseDto(500, "INTERNAL SERVER ERROR");
        }
    }

    @Transactional
    @Override
    public CrudOperationResponseDto<UserResponseDto>
    updateUserStatusAndRole(long userId, boolean status, String roleEnum) {
        try {
            Optional<User> requestedUserOptional = getUserById(userId);
            if (requestedUserOptional.isPresent()) {
                User existingUser = requestedUserOptional.get();
                existingUser.setStatus(status);

                Optional<Roles> roleOptional = rolesRepository.findByRoleEnum(ERole.valueOf(roleEnum));
                if (!roleOptional.isPresent()) {
                    return new CrudOperationResponseDto(401, "ROLE NOT FOUND");
                }
                existingUser.setRole(roleOptional.get());
                User savedUser = userRepository.save(existingUser);
                return new CrudOperationResponseDto(200, "OPERATION SUCCESSFUL", savedUser);
            } else {
                return new CrudOperationResponseDto(404, "USER NOT FOUND");
            }

        } catch (Exception e) {
            return new CrudOperationResponseDto(500, "INTERNAL SERVER ERROR");
        }
    }

    @Transactional
    @Override
    public CrudOperationResponseDto deleteUser(long userId) {
        try {
            Optional<User> requestedUserOptional = getUserById(userId);
            if (requestedUserOptional.isPresent()) {
                userRepository.deleteUserById(requestedUserOptional.get().getId());
                return new CrudOperationResponseDto(200, "OPERATION SUCCESSFUL");
            } else {
                return new CrudOperationResponseDto(404, "USER NOT FOUND");
            }
        } catch (Exception e) {
            return new CrudOperationResponseDto(500, "INTERNAL SERVER ERROR");
        }
    }

}