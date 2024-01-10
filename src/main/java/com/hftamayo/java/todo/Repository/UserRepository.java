package com.hftamayo.java.todo.Repository;

import com.hftamayo.java.todo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    @Override
    List<User> findAll();
    List<User> findAllByActive(boolean isActive);
    User findByUsername(String username);
    User findByEmail(String email);
    User findByUsernameAndPassword(String username, String password);
    User findByEmailAndPassword(String email, String password);
    long countAllByUsername(String username);
    long countAllByEmail(String email);

}