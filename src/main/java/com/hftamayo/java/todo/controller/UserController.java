package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.auth.RegisterUserResponseDto;
import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.model.User;
import com.hftamayo.java.todo.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users/manager")
public class UserController {
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping(value = "/allusers")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping(value = "/getuserbyid/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<UserResponseDto> getUser(@PathVariable long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping(value = "/getuserbyname/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<User> getUserByUsername(@PathVariable String username) {
        return userService.getUserByName(username);
    }

    @GetMapping(value = "/getuserbyemail/{email}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<User> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping(value = "/getuserbynandp/{username}/{password}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<User> getUserByUsernameAndPassword(@PathVariable String username, @PathVariable String password) {
        return userService.getUserByNameAndPassword(username, password);
    }

    @GetMapping(value = "/getuserbyeandp/{email}/{password}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<User> getUserByEmailAndPassword(@PathVariable String email, @PathVariable String password) {
        return userService.getUserByEmailAndPassword(email, password);
    }

    @GetMapping(value = "/countbycriteria/{criteria}/{value}")
    @ResponseStatus(HttpStatus.OK)
    public long countUserByUsername(@PathVariable String criteria, @PathVariable String value) {
        return userService.countAllByCriteria(criteria, value);
    }

    @PostMapping(value = "/saveuser")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterUserResponseDto saveUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return userService.userToDto(savedUser);
    }

    @PutMapping(value = "/updateuser/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable long userId, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(userId, user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (EntityNotFoundException enf) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PatchMapping(value = "/updatestatus/{userId}")
    public ResponseEntity<User> updateUserStatus(@PathVariable long userId, @RequestParam boolean status) {
        try {
            User updatedUser = userService.updateUserStatus(userId, status);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (EntityNotFoundException enf) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping(value = "/activateuser/{userId}")
    public ResponseEntity<User> updateUserStatusAndRole(@PathVariable long userId,
                                                        @RequestBody Map<String, Object> updates) {
        try {
            boolean status = (boolean) updates.get("status");
            String roleEnum = (String) updates.get("role");

            User updatedUser = userService.updateUserStatusAndRole(userId, status, roleEnum);

            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (EntityNotFoundException enf) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("unexpected error: ", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/deleteuser/{userId}")
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
