package com.hftamayo.java.todo.repository;

import com.hftamayo.java.todo.entity.ERole;
import com.hftamayo.java.todo.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {

    @Override
    List<Roles> findAll();
    Optional<Roles> findRolesById(long id);
    Optional<Roles> findByRoleEnum(ERole roleEnum);
    void deleteRolesById(long id);

}
