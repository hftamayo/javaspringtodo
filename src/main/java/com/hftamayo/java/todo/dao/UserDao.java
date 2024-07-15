package com.hftamayo.java.todo.dao;

import com.hftamayo.java.todo.model.User;
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
public class UserDao {

    @PersistenceContext
    public EntityManager entityManager;

    public List<User> getUsers() {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root).orderBy(builder.desc(root.get("id")));

            return entityManager.createQuery(query).getResultList();

        } catch (PersistenceException pe) {
            throw new RuntimeException("Error retrieving roles", pe);
        }
    }

    public Optional<User> getUserById(long userId) {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root).where(builder.equal(root.get("id"), userId));

            User user = entityManager.createQuery(query).getSingleResult();
            return Optional.ofNullable(user);
        } catch (PersistenceException pe) {
            throw new RuntimeException("Error retrieving data: not found", pe);
        }
    }

    public List<User> getUsersByStatus(boolean isActive) {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root).where(builder.equal(root.get("status"), isActive));

            return entityManager.createQuery(query).getResultList();
        } catch (PersistenceException pe) {
            throw new RuntimeException("Error retrieving data: not found", pe);
        }
    }

    public Optional<List<User>> getUserByCriteria(String criteria, String value) {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root).where(builder.equal(root.get(criteria), value));

            List<User> userList = entityManager.createQuery(query).getResultList();
            return Optional.ofNullable(userList.isEmpty() ? null : userList);
        } catch (PersistenceException pe) {
            throw new RuntimeException("Error retrieving data: not found", pe);
        }
    }

    public Optional<List<User>> getUserByCriterias(String criteria, String value, String criteria2, String value2) {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root).where(builder.equal(root.get(criteria), value),
                    builder.equal(root.get(criteria2), value2));
            List<User> userList = entityManager.createQuery(query).getResultList();
            return Optional.ofNullable(userList.isEmpty() ? null : userList);
        } catch (PersistenceException pe) {
            throw new RuntimeException("Error retrieving data: not found", pe);
        }
    }

    public Optional<User> getUserByNameAndPassword(String userName, String userPassword) {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root).where(builder.equal(root.get("name"), userName),
                    builder.equal(root.get("password"), userPassword));

            User user = entityManager.createQuery(query).getSingleResult();
            return Optional.ofNullable(user);
        } catch (PersistenceException pe) {
            throw new RuntimeException("Error retrieving data: not found", pe);
        }
    }

    public Optional<User> getUserByEmailAndPassword(String userEmail, String userPassword) {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root).where(builder.equal(root.get("email"), userEmail),
                    builder.equal(root.get("password"), userPassword));

            User user = entityManager.createQuery(query).getSingleResult();
            return Optional.ofNullable(user);
        } catch (PersistenceException pe) {
            throw new RuntimeException("Error retrieving data: not found", pe);
        }
    }

    public User saveUser(User newUser) {
        try {
            entityManager.getTransaction().begin();
            User existingUser = entityManager.find(User.class, newUser.getId());
            if (existingUser == null) {
                entityManager.persist(newUser);
            } else {
                newUser = entityManager.merge(newUser);
            }
            entityManager.getTransaction().commit();
            return newUser;
        } catch (PersistenceException pe) {
            throw new RuntimeException("Error saving user", pe);
        }
    }

    public User updateUser(long userId, User updatedUser) {
        try {
            entityManager.getTransaction().begin();
            User existingUser = entityManager.find(User.class, userId);
            if (existingUser != null) {
                existingUser.setName(updatedUser.getName());
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setPassword(updatedUser.getPassword());
                existingUser.setAge(updatedUser.getAge());
                existingUser.setStatus(updatedUser.isStatus());
                existingUser.setRole(updatedUser.getRole());
                existingUser = entityManager.merge(existingUser);
            }
            entityManager.getTransaction().commit();
            return existingUser;
        } catch (PersistenceException pe) {
            throw new RuntimeException("Error updating user", pe);
        }
    }

    public void deleteUser(long userId) {
        try {
            entityManager.getTransaction().begin();
            User user = entityManager.find(User.class, userId);
            if (user != null) {
                entityManager.remove(user);
            }
            entityManager.getTransaction().commit();
        } catch (PersistenceException pe) {
            throw new RuntimeException("Error deleting user", pe);
        }
    }

}
