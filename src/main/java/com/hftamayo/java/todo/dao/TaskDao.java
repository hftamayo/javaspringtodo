package com.hftamayo.java.todo.dao;

import com.hftamayo.java.todo.model.Task;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskDao {

    @PersistenceContext
    public EntityManager entityManager;

    public List<Task> getTasks() {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Task> query = builder.createQuery(Task.class);
            Root<Task> root = query.from(Task.class);
            query.select(root).orderBy(builder.desc(root.get("id")));

            return entityManager.createQuery(query).getResultList();

        } catch (PersistenceException pe) {
            throw new RuntimeException("Error retrieving tasks", pe);
        }
    }

    public List<Task> getTasksByStatus(boolean isActive) {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Task> query = builder.createQuery(Task.class);
            Root<Task> root = query.from(Task.class);
            query.select(root).where(builder.equal(root.get("status"), isActive));

            return entityManager.createQuery(query).getResultList();
        } catch (PersistenceException pe) {
            throw new RuntimeException("Error retrieving tasks", pe);
        }
    }

    public Optional<Task> getTaskById(long taskId) {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Task> query = builder.createQuery(Task.class);
            Root<Task> root = query.from(Task.class);
            query.select(root).where(builder.equal(root.get("id"), taskId));

            Task task = entityManager.createQuery(query).getSingleResult();
            return Optional.ofNullable(task);
        } catch (PersistenceException pe) {
            throw new RuntimeException("Error retrieving tasks", pe);
        }
    }

    public Optional<Task> getTaskByTitle(String taskTitle) {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Task> query = builder.createQuery(Task.class);
            Root<Task> root = query.from(Task.class);
            query.select(root).where(builder.equal(root.get("title"), taskTitle));

            Task task = entityManager.createQuery(query).getSingleResult();
            return Optional.ofNullable(task);
        } catch (PersistenceException pe) {
            throw new RuntimeException("Error retrieving tasks", pe);
        }
    }








}
