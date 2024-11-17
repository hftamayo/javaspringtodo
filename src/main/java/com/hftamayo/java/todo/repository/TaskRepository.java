package com.hftamayo.java.todo.repository;

import com.hftamayo.java.todo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAll();
    Optional<Task> findTaskById(long id);
    void deleteTaskById(long id);
}
