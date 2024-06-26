package com.hftamayo.java.todo.repository;

import com.hftamayo.java.todo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    @Override
    List<User> findAll();
    List<User> findAllByStatus(boolean isActive);
    Optional<User> findByName(String username);
    Optional <User> findByEmail(String email);
    User findByNameAndPassword(String username, String password);
    User findByEmailAndPassword(String email, String password);
    boolean existsByName(String username);
    long countAllByName(String username);
    long countAllByEmail(String email);

}
