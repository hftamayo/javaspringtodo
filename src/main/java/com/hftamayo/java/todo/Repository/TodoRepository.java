package com.hftamayo.java.todo.Repository;

import com.hftamayo.java.todo.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Task, Long> {

    @Override
    List<Task> findAll();
    Task findByTitle(String title);

    long countAllByStatus(boolean isActive);

    //for next release
    // List<Task> findAllByOwner(long taskOwner);
    //    long countAllByOwner(long taskOwner);

}
