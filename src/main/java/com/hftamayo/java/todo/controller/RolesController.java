package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.EndpointResponseDto;
import com.hftamayo.java.todo.dto.pagination.PageRequestDto;
import com.hftamayo.java.todo.dto.roles.RolesResponseDto;
import com.hftamayo.java.todo.entity.Roles;
import com.hftamayo.java.todo.services.RolesService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.hftamayo.java.todo.dto.pagination.PaginatedDataDto;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/roles")
public class RolesController {
    private final RolesService rolesService;

    @GetMapping(value = "/list")
    @ResponseStatus(HttpStatus.OK)
    public EndpointResponseDto<PaginatedDataDto<RolesResponseDto>> getRoles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "#{${pagination.default-page-size:10}}") int size,
            @RequestParam(required = false) String sort) {
        PageRequestDto pageRequestDto = new PageRequestDto(page, size, sort);
        PaginatedDataDto<RolesResponseDto> paginatedData = rolesService.getPaginatedRoles(pageRequestDto);
        return new EndpointResponseDto<>(200, "OPERATION_SUCCESS", paginatedData);
    }

    @GetMapping(value = "/rolebn/{roleName}")
    @ResponseStatus(HttpStatus.OK)
    public EndpointResponseDto<RolesResponseDto> getRoleByName(@PathVariable String name) {
        return rolesService.getRoleByName(name);
    }

    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointResponseDto<RolesResponseDto> saveRole(@RequestBody Roles role) {
        return rolesService.saveRole(role);
    }

    @PatchMapping(value = "/update/{roleId}")
    @ResponseStatus(HttpStatus.OK)
    public EndpointResponseDto<RolesResponseDto> updateRole(@PathVariable long roleId, @RequestBody Roles role) {
            return rolesService.updateRole(roleId, role);
    }

    @DeleteMapping(value = "/delete/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public EndpointResponseDto<RolesResponseDto> deleteRole(@PathVariable long roleId) {
            return rolesService.deleteRole(roleId);
    }
}
