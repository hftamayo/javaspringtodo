package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.CrudOperationResponseDto;
import com.hftamayo.java.todo.dto.roles.RolesResponseDto;
import com.hftamayo.java.todo.entity.Roles;
import com.hftamayo.java.todo.services.RolesService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/roles")
public class RolesController {
    private final RolesService rolesService;

    @GetMapping(value = "/list")
    @ResponseStatus(HttpStatus.OK)
    public CrudOperationResponseDto<RolesResponseDto> getRoles() {
        return rolesService.getRoles();
    }

    @GetMapping(value = "/rolebn/{roleName}")
    @ResponseStatus(HttpStatus.OK)
    public CrudOperationResponseDto<RolesResponseDto> getRoleByName(@PathVariable String name) {
        return rolesService.getRoleByName(name);
    }

    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public CrudOperationResponseDto<RolesResponseDto> saveRole(@RequestBody Roles role) {
        return rolesService.saveRole(role);
    }

    @PatchMapping(value = "/update/{roleId}")
    @ResponseStatus(HttpStatus.OK)
    public CrudOperationResponseDto<RolesResponseDto> updateRole(@PathVariable long roleId, @RequestBody Roles role) {
            return rolesService.updateRole(roleId, role);
    }

    @DeleteMapping(value = "/delete/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public CrudOperationResponseDto<RolesResponseDto> deleteRole(@PathVariable long roleId) {
            return rolesService.deleteRole(roleId);
    }
}
