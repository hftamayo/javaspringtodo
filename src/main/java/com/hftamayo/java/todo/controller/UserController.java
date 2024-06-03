package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.RegisterUserResponseDto;
import com.hftamayo.java.todo.model.ERole;
import com.hftamayo.java.todo.model.Roles;
import com.hftamayo.java.todo.model.User;
import com.hftamayo.java.todo.services.RolesService;
import com.hftamayo.java.todo.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final RolesService roleService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping(value = "/supervisor/allusers")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping(value = "/supervisor/getuserbyid/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<User> getUser(@PathVariable long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping(value = "/supervisor/getuserbyusername/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<User> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping(value = "/supervisor/getuserbyemail/{email}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<User> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping(value = "/supervisor/getuserbyusernameandpassword/{username}/{password}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserByUsernameAndPassword(@PathVariable String username, @PathVariable String password) {
        return userService.getUserByUsernameAndPassword(username, password);
    }

    @GetMapping(value = "/supervisor/getuserbyemailandpassword/{email}/{password}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserByEmailAndPassword(@PathVariable String email, @PathVariable String password) {
        return userService.getUserByEmailAndPassword(email, password);
    }

    @GetMapping(value = "/supervisor/countuserbyusername/{username}")
    @ResponseStatus(HttpStatus.OK)
    public long countUserByUsername(@PathVariable String username) {
        return userService.countAllUserByUsername(username);
    }

    @GetMapping(value = "/supervisor/countuserbyemail/{email}")
    @ResponseStatus(HttpStatus.OK)
    public long countUserByEmail(@PathVariable String email) {
        return userService.countAllUserByEmail(email);
    }

    @PostMapping(value = "/supervisor/saveuser")
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


    @PatchMapping(value = "/supervisor/updatestatus/{userId}")
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

    @PatchMapping(value = "/supervisor/activateuser/{userId}")
    public ResponseEntity<User> updateUserStatusAndRole(@PathVariable long userId,
                                                        @RequestBody Map<String, Object> updates) {
        try {
            boolean status = (boolean) updates.get("status");
            String roleEnum = (String) updates.get("role");

            userService.updateUserStatus(userId, status);
            userService.updateUserRole(userId, roleEnum);

            //User updatedUser = userService.updateUser(userId, user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (EntityNotFoundException enf) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("unexpected error: ", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/admin/deleteuser/{userId}")
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
