package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.entity.User;
import com.hftamayo.java.todo.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users/manager")
public class UserController {
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping(value = "/list")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping(value = "/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<UserResponseDto> getUser(@PathVariable long userId) {
        return userService.getUser(userId);
    }

    @GetMapping(value = "/userbc/{criteria}/{value}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<UserResponseDto>> getUserByCriteria(@PathVariable String criteria, @PathVariable String value) {
        return userService.getUserByCriteria(criteria, value);
    }

    @GetMapping(value = "/userbcs/{criteria}/{value}/{criteria2}/{value2}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<UserResponseDto>> getUserByCriterias(@PathVariable String criteria, @PathVariable String value, @PathVariable String criteria2, @PathVariable String value2) {
        return userService.getUserByCriterias(criteria, value, criteria2, value2);
    }

    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PutMapping(value = "/update/{userId}")
    public UserResponseDto updateUser(@PathVariable long userId, @RequestBody User user) {
        try {
            return userService.updateUser(userId, user);
        } catch (EntityNotFoundException enf) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @PatchMapping(value = "/status/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto updateUserStatus(@PathVariable long userId, @RequestBody Map<String, Object> updates) {
        try {
            boolean status = (boolean) updates.get("status");
            return userService.updateUserStatus(userId, status);
        } catch (EntityNotFoundException enf) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(value = "/activate/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto updateUserStatusAndRole(@PathVariable long userId, @RequestBody Map<String, Object> updates) {
        try {
            boolean status = (boolean) updates.get("status");
            String roleEnum = (String) updates.get("role");
            return userService.updateUserStatusAndRole(userId, status, roleEnum);
        } catch (EntityNotFoundException enf) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/delete/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteUser(@PathVariable long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException enf) {
            return new ResponseEntity<>(enf.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
