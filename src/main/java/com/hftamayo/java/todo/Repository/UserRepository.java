package com.hftamayo.java.todo.Repository;

import com.hftamayo.java.todo.Model.User;
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
    User findByEmail(String email);
    User findByNameAndPassword(String username, String password);
    User findByEmailAndPassword(String email, String password);
    Optional<User> findByUsernameOrEmail(String username, String email);
    boolean existsByName(String username);
    long countAllByName(String username);
    long countAllByEmail(String email);

}
