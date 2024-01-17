package com.hftamayo.java.todo.Repository;

import com.hftamayo.java.todo.Model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolesRepository extends JpaRepository<Roles, Long> {
    @Override
    List<Roles> findAll();
    Roles findByName(String name);
}
