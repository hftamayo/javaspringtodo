package com.hftamayo.java.todo.Repository;

import com.hftamayo.java.todo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    @Modifying
    @Query(value = "ALTER TABLE user AUTO_INCREMENT = 1" , nativeQuery = true)
    void resetAutoIncrement();

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
