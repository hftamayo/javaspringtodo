package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.EndpointResponseDto;
import com.hftamayo.java.todo.dto.pagination.PageRequestDto;
import com.hftamayo.java.todo.dto.roles.RolesResponseDto;
import com.hftamayo.java.todo.entity.Roles;
import com.hftamayo.java.todo.services.RolesService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.hftamayo.java.todo.dto.pagination.PaginatedDataDto;
import com.hftamayo.java.todo.utilities.endpoints.ResponseUtil;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/roles")
public class RolesController {
    private final RolesService rolesService;

    @GetMapping(value = "/list")
    public ResponseEntity<EndpointResponseDto<?>> getRoles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "#{${pagination.default-page-size:10}}") int size,
            @RequestParam(required = false) String sort) {
        try {
            PageRequestDto pageRequestDto = new PageRequestDto(page, size, sort);
            PaginatedDataDto<RolesResponseDto> paginatedData = rolesService.getPaginatedRoles(pageRequestDto);
            EndpointResponseDto<PaginatedDataDto<RolesResponseDto>> response =
                    ResponseUtil.successResponse(paginatedData, "OPERATION_SUCCESS");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(
                ResponseUtil.errorResponse
                        (HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch roles list", e),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping(value = "/rolebn/{roleName}")
    public ResponseEntity<EndpointResponseDto<?>> getRoleByName(@PathVariable String name) {
        try {
            EndpointResponseDto<RolesResponseDto> response = rolesService.getRoleByName(name);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(
                ResponseUtil.errorResponse(HttpStatus.NOT_FOUND, "Role not found", e),
                HttpStatus.NOT_FOUND
            );
        }
    }

    @PostMapping(value = "/create")
    public ResponseEntity<EndpointResponseDto<?>> saveRole(@RequestBody Roles role) {
        try {
            EndpointResponseDto<RolesResponseDto> response =
                    ResponseUtil.createdResponse(rolesService.saveRole(role).getData(), "ROLE_CREATED");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(
                ResponseUtil.errorResponse(HttpStatus.BAD_REQUEST, "Failed to create role", e),
                HttpStatus.BAD_REQUEST
            );
        }
    }

    @PatchMapping(value = "/update/{roleId}")
    public ResponseEntity<EndpointResponseDto<?>> updateRole(@PathVariable long roleId, @RequestBody Roles role) {
        try {
            EndpointResponseDto<RolesResponseDto> response =
                    ResponseUtil.successResponse(rolesService.updateRole(roleId, role).getData(), "ROLE_UPDATED");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(
                ResponseUtil.errorResponse(HttpStatus.BAD_REQUEST, "Failed to update role", e),
                HttpStatus.BAD_REQUEST
            );
        }
    }

    @DeleteMapping(value = "/delete/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EndpointResponseDto<?>> deleteRole(@PathVariable long roleId) {
        try {
            rolesService.deleteRole(roleId);
            EndpointResponseDto<Void> response = ResponseUtil.successResponse(null, "ROLE_DELETED");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(
                ResponseUtil.errorResponse(HttpStatus.BAD_REQUEST, "Failed to delete role", e),
                HttpStatus.BAD_REQUEST
            );
        }
    }
}
