package com.hftamayo.java.todo.repository;

import com.hftamayo.java.todo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Override
    List<Task> findAll();
    List <Task> findAllByStatus(boolean isActive);
    Task findByTitle(String title);
    long countAllByStatus(boolean isActive);

    //for next release
    // List<Task> findAllByOwner(long taskOwner);
    //    long countAllByOwner(long taskOwner);

}
