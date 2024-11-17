package com.hftamayo.java.todo.repository;

import com.hftamayo.java.todo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @Override
    List<User> findAll();
    Optional<User> findUserById(long id);
    Optional<User> findUserByEmail(String email);
    Optional<User> findByUsername(String username);
    void deleteUserById(long id);
}
