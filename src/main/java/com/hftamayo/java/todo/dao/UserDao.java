package com.hftamayo.java.todo.dao;

import com.hftamayo.java.todo.model.Roles;
import com.hftamayo.java.todo.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Optional<User> getUsersByStatus(boolean isActive) {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root).where(builder.equal(root.get("status"), isActive));

            User user = entityManager.createQuery(query).getSingleResult();
            return Optional.ofNullable(user);
        } catch (PersistenceException pe) {
            throw new RuntimeException("Error retrieving data: not found", pe);
        }
    }

    public Optional<User> getUserByEmail(String userEmail) {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root).where(builder.equal(root.get("email"), userEmail));

            User user = entityManager.createQuery(query).getSingleResult();
            return Optional.ofNullable(user);
        } catch (PersistenceException pe) {
            throw new RuntimeException("Error retrieving data: not found", pe);
        }
    }

    public Optional<User> getUserByName(String userName) {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root).where(builder.equal(root.get("name"), userName));

            User user = entityManager.createQuery(query).getSingleResult();
            return Optional.ofNullable(user);
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

    public long countAllByCriteria(String criteria, String value) {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root).where(builder.equal(root.get(criteria), value));

            return entityManager.createQuery(query).getResultList().size();
        } catch (PersistenceException pe) {
            throw new RuntimeException("Error retrieving data: not found", pe);
        }
    }

    public User saveUser(User newUser) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(newUser);
            session.getTransaction().commit();
            return newUser;
        } catch (HibernateException he) {
            throw new RuntimeException("Error retrieving user", he);
        }
    }

    public User updateUser(long userId, User updatedUser) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = session.get(User.class, userId);
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            user.setAge(updatedUser.getAge());
            user.setStatus(updatedUser.isStatus());
            user.setRole(updatedUser.getRole());
            User mergedUser = (User) session.merge(user);
            session.getTransaction().commit();
            return mergedUser;
        } catch (HibernateException he) {
            throw new RuntimeException("Error retrieving user", he);
        }
    }

    public void deleteUser(long userId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = session.get(User.class, userId);
            session.remove(user);
            session.getTransaction().commit();
        } catch (HibernateException he) {
            throw new RuntimeException("Error retrieving user", he);
        }
    }

}
