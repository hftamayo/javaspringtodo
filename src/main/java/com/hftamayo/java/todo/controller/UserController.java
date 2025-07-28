package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.EndpointResponseDto;
import com.hftamayo.java.todo.dto.pagination.PageRequestDto;
import com.hftamayo.java.todo.dto.pagination.PaginatedDataDto;
import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.entity.User;
import com.hftamayo.java.todo.services.UserService;
import com.hftamayo.java.todo.utilities.endpoints.ResponseUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users/manager")
public class UserController {
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping(value = "/list")
    public ResponseEntity<EndpointResponseDto<?>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "#{${pagination.default-page-size:10}}") int size,
            @RequestParam(required = false) String sort) {
        try {
            PageRequestDto pageRequestDto = new PageRequestDto(page, size, sort);
            PaginatedDataDto<UserResponseDto> paginatedData = userService.getPaginatedUsers(pageRequestDto);
            EndpointResponseDto<PaginatedDataDto<UserResponseDto>> response = ResponseUtil.successResponse(paginatedData, "OPERATION_SUCCESS");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(
                ResponseUtil.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch users list", e),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<EndpointResponseDto<?>> getUser(@PathVariable long userId) {
        try {
            UserResponseDto user = userService.getUser(userId);
            EndpointResponseDto<UserResponseDto> response = ResponseUtil.successResponse(user, "OPERATION_SUCCESS");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(
                ResponseUtil.errorResponse(HttpStatus.NOT_FOUND, "User not found", e),
                HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping(value = "/userbc/{criteria}/{value}")
    public ResponseEntity<EndpointResponseDto<?>> getUserByCriteria(@PathVariable String criteria, @PathVariable String value) {
        try {
            UserResponseDto user = userService.getUserByCriteria(criteria, value);
            EndpointResponseDto<UserResponseDto> response = ResponseUtil.successResponse(user, "OPERATION_SUCCESS");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(
                ResponseUtil.errorResponse(HttpStatus.NOT_FOUND, "User not found by criteria", e),
                HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping(value = "/userbcs/{criteria}/{value}/{criteria2}/{value2}")
    public ResponseEntity<EndpointResponseDto<?>> getUserByCriterias(@PathVariable String criteria, @PathVariable String value,
                       @PathVariable String criteria2, @PathVariable String value2) {
        try {
            UserResponseDto user = userService.getUserByCriterias(criteria, value, criteria2, value2);
            EndpointResponseDto<UserResponseDto> response = ResponseUtil.successResponse(user, "OPERATION_SUCCESS");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(
                ResponseUtil.errorResponse(HttpStatus.NOT_FOUND, "User not found by criterias", e),
                HttpStatus.NOT_FOUND
            );
        }
    }

    @PostMapping(value = "/create")
    public ResponseEntity<EndpointResponseDto<?>> saveUser(@RequestBody User user) {
        try {
            UserResponseDto savedUser = userService.saveUser(user);
            EndpointResponseDto<UserResponseDto> response = ResponseUtil.createdResponse(savedUser, "USER_CREATED");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(
                ResponseUtil.errorResponse(HttpStatus.BAD_REQUEST, "Failed to create user", e),
                HttpStatus.BAD_REQUEST
            );
        }
    }

    @PatchMapping(value = "/update/{userId}")
    public ResponseEntity<EndpointResponseDto<?>> updateUser(@PathVariable long userId, @RequestBody User user) {
        try {
            UserResponseDto updatedUser = userService.updateUser(userId, user);
            EndpointResponseDto<UserResponseDto> response = ResponseUtil.successResponse(updatedUser, "USER_UPDATED");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(
                ResponseUtil.errorResponse(HttpStatus.BAD_REQUEST, "Failed to update user", e),
                HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping(value = "/status/{userId}")
    public ResponseEntity<EndpointResponseDto<?>> updateUserStatus(@PathVariable long userId, @RequestBody Map<String, Object> updates) {
        try {
            boolean status = (boolean) updates.get("status");
            UserResponseDto updatedUser = userService.updateUserStatus(userId, status);
            EndpointResponseDto<UserResponseDto> response = ResponseUtil.successResponse(updatedUser, "USER_STATUS_UPDATED");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(
                ResponseUtil.errorResponse(HttpStatus.BAD_REQUEST, "Failed to update user status", e),
                HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping(value = "/activate/{userId}")
    public ResponseEntity<EndpointResponseDto<?>> updateUserStatusAndRole(@PathVariable long userId, @RequestBody Map<String, Object> updates) {
        try {
            boolean status = (boolean) updates.get("status");
            String roleEnum = (String) updates.get("role");
            UserResponseDto updatedUser = userService.updateUserStatusAndRole(userId, status, roleEnum);
            EndpointResponseDto<UserResponseDto> response = ResponseUtil.successResponse(updatedUser, "USER_STATUS_AND_ROLE_UPDATED");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(
                ResponseUtil.errorResponse(HttpStatus.BAD_REQUEST, "Failed to update user status and role", e),
                HttpStatus.BAD_REQUEST
            );
        }
    }

    @DeleteMapping(value = "/delete/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EndpointResponseDto<?>> deleteUser(@PathVariable long userId) {
        try {
            userService.deleteUser(userId);
            EndpointResponseDto<Void> response = ResponseUtil.successResponse(null, "USER_DELETED");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(
                ResponseUtil.errorResponse(HttpStatus.BAD_REQUEST, "Failed to delete user", e),
                HttpStatus.BAD_REQUEST
            );
        }
    }
}
