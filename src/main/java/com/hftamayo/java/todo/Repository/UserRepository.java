package com.hftamayo.java.todo.Repository;

import com.hftamayo.java.todo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    @Override
    List<User> findAll();
    List<User> findAllByStatus(boolean isActive);
    User findByName(String username);
    User findByEmail(String email);
    User findByNameAndPassword(String username, String password);
    User findByEmailAndPassword(String email, String password);
    User findByUsernameOrEmail(String username, String email);
    long countAllByName(String username);
    long countAllByEmail(String email);

}
