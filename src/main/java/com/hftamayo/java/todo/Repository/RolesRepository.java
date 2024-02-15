package com.hftamayo.java.todo.Repository;

import com.hftamayo.java.todo.Model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {
    @Modifying
    @Query(value = "ALTER TABLE roles AUTO_INCREMENT = 1" , nativeQuery = true)
    void resetAutoIncrement();

    @Override
    List<Roles> findAll();
    Optional<Roles> findByName(String name);
}
