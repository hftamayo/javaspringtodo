package com.hftamayo.java.todo.repository;

import com.hftamayo.java.todo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    @Override
    List<Task> findAll();
    Optional<Task> findTaskById(long id);
    Optional<Task> findTaskByTitle(String title);
    Optional<Task> deleteTaskById(long id);
}
