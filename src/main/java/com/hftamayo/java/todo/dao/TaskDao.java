package com.hftamayo.java.todo.dao;

import com.hftamayo.java.todo.dto.task.TasksByStatusResponseDto;
import com.hftamayo.java.todo.model.Roles;
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

    public TasksByStatusResponseDto getTasksByStatusAndSize(boolean isActive) {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Task> query = builder.createQuery(Task.class);
            Root<Task> root = query.from(Task.class);
            query.select(root).where(builder.equal(root.get("status"), isActive));

            List<Task> tasks = entityManager.createQuery(query).getResultList();
            return new TasksByStatusResponseDto(tasks);
        } catch (PersistenceException pe) {
            throw new RuntimeException("Error retrieving tasks", pe);
        }
    }

    public Task saveTask(Task task) {
        try {
            entityManager.getTransaction().begin();
            Task existingTask = entityManager.find(Task.class, task.getId());
            if (existingTask == null) {
                entityManager.persist(task);
            } else {
                task = entityManager.merge(task);
            }
            entityManager.getTransaction().commit();
            return task;
        } catch (PersistenceException pe) {
            throw new RuntimeException("Error saving task", pe);
        }
    }

    public Task updateTask(long taskId, Task task) {
        try {
            entityManager.getTransaction().begin();
            Task existingTask = entityManager.find(Task.class, taskId);
            if (existingTask != null) {
                existingTask.setTitle(task.getTitle());
                existingTask.setDescription(task.getDescription());
                existingTask.setStatus(task.isStatus());
                existingTask = entityManager.merge(existingTask);
            }
            entityManager.getTransaction().commit();
            return existingTask;
        } catch (PersistenceException pe) {
            throw new RuntimeException("Error updating task", pe);
        }
    }

    public void deleteTask(long taskId) {
        try {
            entityManager.getTransaction().begin();
            Task task = entityManager.find(Task.class, taskId);
            if (task != null) {
                entityManager.remove(task);
            }
            entityManager.getTransaction().commit();
        } catch (PersistenceException pe) {
            throw new RuntimeException("Error deleting task", pe);
        }
    }
}
