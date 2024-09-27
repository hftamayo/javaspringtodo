package com.hftamayo.java.todo.dao;

import com.hftamayo.java.todo.model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.*;

public class TaskDaoTest {
    private TaskDao taskDao;
    private Task task;
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        entityManager = Mockito.mock(EntityManager.class);
        taskDao = new TaskDao();
        taskDao.entityManager = entityManager;

        task = Task.builder()
                .id(1L)
                .title("Task 1")
                .description("Task 1 description")
                .status(true)
                .dateAdded(LocalDateTime.now())
                .dateUpdated(LocalDateTime.now())
                .build();
    }

    @Test
    public void testGetTasks() {
        CriteriaBuilder builder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<Task> query = Mockito.mock(CriteriaQuery.class);
        Root<Task> root = Mockito.mock(Root.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(builder);
        Mockito.when(builder.createQuery(Task.class)).thenReturn(query);
        Mockito.when(query.from(Task.class)).thenReturn(root);
        Mockito.when(query.select(root)).thenReturn(query);
        Mockito.when(entityManager.createQuery(query).getResultList()).thenReturn(Collections.singletonList(task));

        List<Task> tasks = taskDao.getTasks();
        Assertions.assertFalse(tasks.isEmpty());
        Assertions.assertEquals(1, tasks.size());
        Assertions.assertEquals(task, tasks.get(0));
    }

    @Test
    public void testGetTaskById() {
        CriteriaBuilder builder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<Task> query = Mockito.mock(CriteriaQuery.class);
        Root<Task> root = Mockito.mock(Root.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(builder);
        Mockito.when(builder.createQuery(Task.class)).thenReturn(query);
        Mockito.when(query.from(Task.class)).thenReturn(root);
        Mockito.when(query.select(root)).thenReturn(query);
        Mockito.when(query.where(builder.equal(root.get("id"), 1L))).thenReturn(query);
        Mockito.when(entityManager.createQuery(query).getSingleResult()).thenReturn(task);

        Optional<Task> foundTask = taskDao.getTaskById(1L);
        Assertions.assertTrue(foundTask.isPresent());
        Assertions.assertEquals(task, foundTask.get());
    }

    @Test
    public void testGetTaskByTitle() {
        CriteriaBuilder builder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<Task> query = Mockito.mock(CriteriaQuery.class);
        Root<Task> root = Mockito.mock(Root.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(builder);
        Mockito.when(builder.createQuery(Task.class)).thenReturn(query);
        Mockito.when(query.from(Task.class)).thenReturn(root);
        Mockito.when(query.select(root)).thenReturn(query);
        Mockito.when(query.where(builder.equal(root.get("title"), "Test Task"))).thenReturn(query);
        Mockito.when(entityManager.createQuery(query).getSingleResult()).thenReturn(task);

        Optional<Task> foundTask = taskDao.getTaskByTitle("Test Task");
        Assertions.assertTrue(foundTask.isPresent());
        Assertions.assertEquals(task, foundTask.get());
    }

    @Test
    public void testGetTaskByCriteria() {
        CriteriaBuilder builder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<Task> query = Mockito.mock(CriteriaQuery.class);
        Root<Task> root = Mockito.mock(Root.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(builder);
        Mockito.when(builder.createQuery(Task.class)).thenReturn(query);
        Mockito.when(query.from(Task.class)).thenReturn(root);
        Mockito.when(query.select(root)).thenReturn(query);
        Mockito.when(query.where(builder.equal(root.get("title"), "Test Task"))).thenReturn(query);
        Mockito.when(entityManager.createQuery(query).getSingleResult()).thenReturn(task);

        Optional<Object> foundTask = taskDao.getTaskByCriteria("title", "Test Task", true);
        Assertions.assertTrue(foundTask.isPresent());
        Assertions.assertEquals(task, foundTask.get());
    }

    @Test
    public void testGetTaskByCriterias() {
        CriteriaBuilder builder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<Task> query = Mockito.mock(CriteriaQuery.class);
        Root<Task> root = Mockito.mock(Root.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(builder);
        Mockito.when(builder.createQuery(Task.class)).thenReturn(query);
        Mockito.when(query.from(Task.class)).thenReturn(root);
        Mockito.when(query.select(root)).thenReturn(query);
        Mockito.when(query.where(builder.equal(root.get("title"), "Test Task"),
                builder.equal(root.get("status"), true))).thenReturn(query);
        Mockito.when(entityManager.createQuery(query).getResultList()).thenReturn(Collections.singletonList(task));

        Optional<List<Task>> foundTasks = taskDao.getTaskByCriterias("title", "Test Task", "status", "true");
        Assertions.assertTrue(foundTasks.isPresent());
        Assertions.assertFalse(foundTasks.get().isEmpty());
        Assertions.assertEquals(1, foundTasks.get().size());
        Assertions.assertEquals(task, foundTasks.get().get(0));
    }

    @Test
    public void testSaveTask() {
        Mockito.when(entityManager.find(Task.class, task.getId())).thenReturn(null);
        Mockito.doNothing().when(entityManager).persist(task);
        Mockito.when(entityManager.merge(task)).thenReturn(task);

        Task savedTask = taskDao.saveTask(task);
        Assertions.assertEquals(task, savedTask);
    }

    @Test
    public void testUpdateTask() {
        Map<String, Object> propertiesToUpdate = new HashMap<>();
        propertiesToUpdate.put("title", "Updated Task");
        propertiesToUpdate.put("status", false);

        Mockito.when(entityManager.find(Task.class, 1L)).thenReturn(task);
        Mockito.when(entityManager.merge(task)).thenReturn(task);

        Task updatedTask = taskDao.updateTask(1L, propertiesToUpdate);

        Assertions.assertEquals("Updated Task", updatedTask.getTitle());
        Assertions.assertFalse(updatedTask.isStatus());
    }

    @Test
    public void testDeleteTask() {
        Mockito.when(entityManager.find(Task.class, 1L)).thenReturn(task);
        Mockito.doNothing().when(entityManager).remove(task);

        taskDao.deleteTask(1L);
        Mockito.verify(entityManager, Mockito.times(1)).remove(task);
    }
}
