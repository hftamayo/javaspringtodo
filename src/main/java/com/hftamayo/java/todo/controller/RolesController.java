package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.CrudOperationResponseDto;
import com.hftamayo.java.todo.dto.roles.RolesResponseDto;
import com.hftamayo.java.todo.entity.Roles;
import com.hftamayo.java.todo.services.RolesService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<RolesResponseDto> getRoles() {
        return rolesService.getRoles();
    }

    @GetMapping(value = "/rolebn/{roleName}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<RolesResponseDto> getRoleByName(@PathVariable String roleName) {
        return rolesService.getRoleByEnum(roleName);
    }

    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public RolesResponseDto saveRole(@RequestBody Roles role) {
        return rolesService.saveRole(role);
    }

    @PutMapping(value = "/update/{roleId}")
    @ResponseStatus(HttpStatus.OK)
    public RolesResponseDto updateRole(@PathVariable long roleId, @RequestBody Roles role) {
        try {
            return rolesService.updateRole(roleId, role);
        } catch (EntityNotFoundException enf) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/delete/{roleId}")
    @ResponseStatus(HttpStatus.OK)
    public CrudOperationResponseDto deleteRole(@PathVariable long roleId) {
        try {
            return rolesService.deleteRole(roleId);
        } catch (EntityNotFoundException enf) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
