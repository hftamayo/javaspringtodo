package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.model.Roles;
import com.hftamayo.java.todo.services.RolesService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/roles")
public class RolesController {
    private final RolesService rolesService;

    @GetMapping(value = "/allroles")
    @ResponseStatus(HttpStatus.OK)
    public List<Roles> getRoles(){
        return rolesService.getRoles();
    }

    @GetMapping(value = "/getrolebyname/{roleName}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Roles> getRoleByName(@PathVariable String roleName){
        return rolesService.getRoleByEnum(roleName);
    }

    @PostMapping(value = "/saverole")
    @ResponseStatus(HttpStatus.CREATED)
    public Roles saveRole(@RequestBody Roles role){
        return rolesService.saveRole(role);
    }

    @PutMapping(value="/updaterole/{roleId}")
    public ResponseEntity<Roles> updateRole(@PathVariable long roleId, @RequestBody Roles role){
        try{
            Roles updatedRole = rolesService.updateRole(roleId, role);
            return new ResponseEntity<>(updatedRole, HttpStatus.OK);
        }catch (EntityNotFoundException enf){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value="/deleterole/{roleId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteRole(@PathVariable long roleId){
        try{
            rolesService.deleteRole(roleId);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (EntityNotFoundException enf){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
