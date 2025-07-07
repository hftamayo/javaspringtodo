package com.hftamayo.java.todo.mapper;

import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.entity.ERole;
import com.hftamayo.java.todo.entity.Roles;
import com.hftamayo.java.todo.entity.User;
import com.hftamayo.java.todo.exceptions.ResourceNotFoundException;
import com.hftamayo.java.todo.repository.RolesRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    private final RolesRepository rolesRepository;

    public UserMapper(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    public UserResponseDto toUserResponseDto(User user) {
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
                user.getRole().getRoleEnum().name()
        );
    }

    public User toEntity(UserResponseDto userResponseDto) {
        User user = new User();
        user.setId(userResponseDto.getId());
        user.setName(userResponseDto.getName());
        user.setEmail(userResponseDto.getEmail());
        user.setAge(userResponseDto.getAge());
        user.setAdmin(userResponseDto.isAdmin());
        user.setAccountNonExpired(userResponseDto.isAccountNonExpired());
        user.setAccountNonLocked(userResponseDto.isAccountNonLocked());
        user.setCredentialsNonExpired(userResponseDto.isCredentialsNonExpired());
        user.setStatus(userResponseDto.isStatus());
        user.setDateAdded(LocalDateTime.parse(userResponseDto.getDateAdded()));
        user.setRole(getRoleByName(userResponseDto.getRole()));
        return user;
    }

    private Roles getRoleByName(String role) {
        return rolesRepository.findByRoleEnum(ERole.valueOf(role))
                .orElseThrow(() -> new ResourceNotFoundException("Role", role));
    }

    public UserResponseDto userToDto(User user) {
        return toUserResponseDto(user);
    }

    public List<UserResponseDto> userListToDto(List<User> users) {
        return users.stream()
                .map(this::toUserResponseDto)
                .collect(Collectors.toList());
    }
}