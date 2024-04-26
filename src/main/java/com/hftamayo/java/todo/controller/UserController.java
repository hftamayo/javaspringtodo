package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.RegisterUserResponseDto;
import com.hftamayo.java.todo.model.Roles;
import com.hftamayo.java.todo.model.User;
import com.hftamayo.java.todo.services.RolesService;
import com.hftamayo.java.todo.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/supervisor")
public class UserController {
    private final UserService userService;
    private final RolesService roleService;

    @Autowired
    public UserController(UserService userService, RolesService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "/users/allusers")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping(value = "/users/getuserbyid/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping(value = "/users/getuserbyusername/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<User> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping(value = "/users/getuserbyemail/{email}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<User> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping(value = "/users/getuserbyusernameandpassword/{username}/{password}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserByUsernameAndPassword(@PathVariable String username, @PathVariable String password) {
        return userService.getUserByUsernameAndPassword(username, password);
    }

    @GetMapping(value = "/users/getuserbyemailandpassword/{email}/{password}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserByEmailAndPassword(@PathVariable String email, @PathVariable String password) {
        return userService.getUserByEmailAndPassword(email, password);
    }

    @GetMapping(value = "/users/countuserbyusername/{username}")
    @ResponseStatus(HttpStatus.OK)
    public long countUserByUsername(@PathVariable String username) {
        return userService.countAllUserByUsername(username);
    }

    @GetMapping(value = "/users/countuserbyemail/{email}")
    @ResponseStatus(HttpStatus.OK)
    public long countUserByEmail(@PathVariable String email) {
        return userService.countAllUserByEmail(email);
    }

    @PostMapping(value = "/users/saveuser")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterUserResponseDto saveUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        RegisterUserResponseDto registerUserResponseDto = new RegisterUserResponseDto();
        registerUserResponseDto.setId(savedUser.getId());
        registerUserResponseDto.setName(savedUser.getName());
        registerUserResponseDto.setEmail(savedUser.getEmail());
        registerUserResponseDto.setAge(savedUser.getAge());
        registerUserResponseDto.setAdmin(savedUser.isAdmin());
        registerUserResponseDto.setStatus(savedUser.isStatus());

        return registerUserResponseDto;
    }

    @PutMapping(value = "/users/updateuser/{userId}")
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

    @PatchMapping(value = "/users/updatestatus/{userId}")
    public ResponseEntity<User> updateUserStatus(@PathVariable long userId, @RequestParam boolean status) {
        try {
            User user = userService.getUserById(userId);
            user.setStatus(status);
            User updatedUser = userService.updateUser(userId, user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (EntityNotFoundException enf) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping(value = "/users/updatestatusandrole/{userId}")
    public ResponseEntity<User> updateUserStatusAndRole(@PathVariable long userId,
                                                        @RequestParam boolean status,
                                                        @RequestParam String roleName) {
        try {
            User user = userService.getUserById(userId);
            user.setStatus(status);

            // Fetch the role by name and add it to the user's roles
            Optional<Roles> roleOptional = roleService.getRoleByName(roleName);
            if (!roleOptional.isPresent()) {
                throw new EntityNotFoundException("Role not found");
            }
            user.getRoles().add(roleOptional.get());

            User updatedUser = userService.updateUser(userId, user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (EntityNotFoundException enf) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping(value = "/users/deleteuser/{userId}")
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
