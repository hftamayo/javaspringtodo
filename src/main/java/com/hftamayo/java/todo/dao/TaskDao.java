package com.hftamayo.java.todo.dao;

import com.hftamayo.java.todo.model.Task;
import com.hftamayo.java.todo.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
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

    public Optional<Object> getTaskByCriteria(String criteria, String value, boolean singleResult) {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Task> query = builder.createQuery(Task.class);
            Root<Task> root = query.from(Task.class);
            query.select(root).where(builder.equal(root.get(criteria), value));

            if (singleResult) {
                Task task = entityManager.createQuery(query).getSingleResult();
                return Optional.ofNullable(task);
            } else {
                List<Task> taskList = entityManager.createQuery(query).getResultList();
                return Optional.ofNullable(taskList.isEmpty() ? null : taskList);
            }
        } catch (PersistenceException pe) {
            throw new RuntimeException("Error retrieving data: not found", pe);
        }
    }

    public Optional<List<Task>> getTaskByCriterias(String criteria, String value, String criteria2, String value2) {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Task> query = builder.createQuery(Task.class);
            Root<Task> root = query.from(Task.class);
            query.select(root).where(builder.equal(root.get(criteria), value),
                    builder.equal(root.get(criteria2), value2));
            List<Task> taskList = entityManager.createQuery(query).getResultList();
            return Optional.ofNullable(taskList.isEmpty() ? null : taskList);
        } catch (PersistenceException pe) {
            throw new RuntimeException("Error retrieving data: not found", pe);
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

    public Task updateTask(long taskId, Map<String, Object> propertiesToUpdate) {
        try {
            entityManager.getTransaction().begin();
            Task existingTask = entityManager.find(Task.class, taskId);
            if (existingTask != null) {
                for (Map.Entry<String, Object> entry : propertiesToUpdate.entrySet()) {
                    Field field = Task.class.getDeclaredField(entry.getKey());
                    field.setAccessible(true);
                    field.set(existingTask, entry.getValue());
                }
                existingTask = entityManager.merge(existingTask);
            }
            entityManager.getTransaction().commit();
            return existingTask;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Error updating task properties", e);
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
