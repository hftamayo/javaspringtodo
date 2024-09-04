package com.hftamayo.java.todo.dao;

import com.hftamayo.java.todo.model.User;
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

public class UserDaoTest {
    private UserDao userDao;
    private User user;
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        entityManager = Mockito.mock(EntityManager.class);
        userDao = new UserDao();
        userDao.entityManager = entityManager;

        user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .password("password")
                .age(30)
                .isAdmin(false)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .status(true)
                .dateAdded(LocalDateTime.now())
                .dateUpdated(LocalDateTime.now())
                .build();
    }

    @Test
    public void testGetUsers() {
        CriteriaBuilder builder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<User> query = Mockito.mock(CriteriaQuery.class);
        Root<User> root = Mockito.mock(Root.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(builder);
        Mockito.when(builder.createQuery(User.class)).thenReturn(query);
        Mockito.when(query.from(User.class)).thenReturn(root);
        Mockito.when(query.select(root)).thenReturn(query);
        Mockito.when(entityManager.createQuery(query).getResultList()).thenReturn(Collections.singletonList(user));

        List<User> users = userDao.getUsers();
        Assertions.assertFalse(users.isEmpty());
        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals(user, users.get(0));
    }

    @Test
    public void testGetUserById() {
        CriteriaBuilder builder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<User> query = Mockito.mock(CriteriaQuery.class);
        Root<User> root = Mockito.mock(Root.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(builder);
        Mockito.when(builder.createQuery(User.class)).thenReturn(query);
        Mockito.when(query.from(User.class)).thenReturn(root);
        Mockito.when(query.select(root)).thenReturn(query);
        Mockito.when(query.where(builder.equal(root.get("id"), 1L))).thenReturn(query);
        Mockito.when(entityManager.createQuery(query).getSingleResult()).thenReturn(user);

        Optional<User> foundUser = userDao.getUserById(1L);
        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertEquals(user, foundUser.get());
    }

    @Test
    public void testGetUserByCriteria() {
        CriteriaBuilder builder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<User> query = Mockito.mock(CriteriaQuery.class);
        Root<User> root = Mockito.mock(Root.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(builder);
        Mockito.when(builder.createQuery(User.class)).thenReturn(query);
        Mockito.when(query.from(User.class)).thenReturn(root);
        Mockito.when(query.select(root)).thenReturn(query);
        Mockito.when(query.where(builder.equal(root.get("email"), "john.doe@example.com"))).thenReturn(query);
        Mockito.when(entityManager.createQuery(query).getSingleResult()).thenReturn(user);

        Optional<Object> foundUser = userDao.getUserByCriteria("email", "john.doe@example.com", true);
        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertEquals(user, foundUser.get());
    }

    @Test
    public void testGetUserByCriterias() {
        CriteriaBuilder builder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<User> query = Mockito.mock(CriteriaQuery.class);
        Root<User> root = Mockito.mock(Root.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(builder);
        Mockito.when(builder.createQuery(User.class)).thenReturn(query);
        Mockito.when(query.from(User.class)).thenReturn(root);
        Mockito.when(query.select(root)).thenReturn(query);
        Mockito.when(query.where(builder.equal(root.get("email"), "john.doe@example.com"),
                builder.equal(root.get("name"), "John Doe"))).thenReturn(query);
        Mockito.when(entityManager.createQuery(query).getResultList()).thenReturn(Collections.singletonList(user));

        Optional<List<User>> foundUsers = userDao.getUserByCriterias("email", "john.doe@example.com", "name", "John Doe");
        Assertions.assertTrue(foundUsers.isPresent());
        Assertions.assertFalse(foundUsers.get().isEmpty());
        Assertions.assertEquals(1, foundUsers.get().size());
        Assertions.assertEquals(user, foundUsers.get().get(0));
    }

    @Test
    public void testSaveUser() {
        Mockito.when(entityManager.find(User.class, user.getId())).thenReturn(null);
        Mockito.doNothing().when(entityManager).persist(user);
        Mockito.when(entityManager.merge(user)).thenReturn(user);

        User savedUser = userDao.saveUser(user);
        Assertions.assertEquals(user, savedUser);
    }

    @Test
    public void testUpdateUser() {
        Map<String, Object> propertiesToUpdate = new HashMap<>();
        propertiesToUpdate.put("name", "Updated Name");
        propertiesToUpdate.put("status", false);

        Mockito.when(entityManager.find(User.class, 1L)).thenReturn(user);
        Mockito.when(entityManager.merge(user)).thenReturn(user);

        User updatedUser = userDao.updateUser(1L, propertiesToUpdate);

        Assertions.assertEquals("Updated Name", updatedUser.getName());
        Assertions.assertFalse(updatedUser.isStatus());
    }

    @Test
    public void testDeleteUser() {
        Mockito.when(entityManager.find(User.class, 1L)).thenReturn(user);
        Mockito.doNothing().when(entityManager).remove(user);

        userDao.deleteUser(1L);
        Mockito.verify(entityManager, Mockito.times(1)).remove(user);
    }

}
