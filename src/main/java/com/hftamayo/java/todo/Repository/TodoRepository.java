package com.hftamayo.java.todo.Repository;

import com.hftamayo.java.todo.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Task, Long> {
}
