package com.hftamayo.java.todo.Controller;

import com.hftamayo.java.todo.Model.Roles;
import com.hftamayo.java.todo.Services.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RolesController {
    private final RolesService rolesService;

    @Autowired
    public RolesController(RolesService rolesService){
        this.rolesService = rolesService;
    }

    @GetMapping(value = "/roles/allroles")
    @ResponseStatus(HttpStatus.OK)
    public List<Roles> getRoles(){
        return rolesService.getRoles();
    }
}
