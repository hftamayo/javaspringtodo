package com.hftamayo.java.todo.dao;

import com.hftamayo.java.todo.entity.ERole;
import com.hftamayo.java.todo.entity.Roles;

import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class RolesDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }


    public List<Roles> getRoles() {
        try {
            Session session = getCurrentSession();
            return session.createQuery("from Roles order by id desc", Roles.class).list();
        } catch (PersistenceException pe) {
            throw new RuntimeException("Error retrieving roles", pe);
        }
    }

    public Optional<Roles> getRoleById(long roleId) {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Roles> query = builder.createQuery(Roles.class);
            Root<Roles> root = query.from(Roles.class);
            query.select(root).where(builder.equal(root.get("id"), roleId));

            Roles role = entityManager.createQuery(query).getSingleResult();
            return Optional.ofNullable(role);
        } catch (PersistenceException pe) {
            throw new RuntimeException("Error retrieving roles", pe);
        }
    }

    public Optional<Roles> getRoleByEnum(String roleEnum) {
        try {
            ERole eRole = ERole.valueOf(roleEnum);

            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Roles> query = builder.createQuery(Roles.class);
            Root<Roles> root = query.from(Roles.class);
            query.select(root).where(builder.equal(root.get("roleEnum"), eRole));

            Roles role = entityManager.createQuery(query).getSingleResult();
            return Optional.ofNullable(role);
        } catch (PersistenceException pe) {
            throw new RuntimeException("Error retrieving roles", pe);
        }
    }

    @Transactional
    public Roles saveRole(Roles newRole) {
        try {
            Roles existingRole = entityManager.find(Roles.class, newRole.getId());
            if (existingRole == null) {
                entityManager.persist(newRole);
            } else {
                newRole = entityManager.merge(newRole);
            }
            return newRole;
        } catch (PersistenceException pe) {
            throw new RuntimeException("Error saving role", pe);
        }
    }

    @Transactional
    public Roles updateRole(long roleId, Map<String, Object> propertiesToUpdate) {
        try {
            Roles existingRole = entityManager.find(Roles.class, roleId);
            if (existingRole != null) {
                for (Map.Entry<String, Object> entry : propertiesToUpdate.entrySet()) {
                    Field field = Roles.class.getDeclaredField(entry.getKey());
                    field.setAccessible(true);
                    field.set(existingRole, entry.getValue());
                }
                existingRole = entityManager.merge(existingRole);
            }
            return existingRole;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Error updating role properties", e);
        } catch (PersistenceException pe) {
            throw new RuntimeException("Error updating role", pe);
        }
    }

    public void deleteRole(long roleId) {
        try {
            entityManager.getTransaction().begin();
            Roles role = entityManager.find(Roles.class, roleId);
            if (role != null) {
                entityManager.remove(role);
            }
            entityManager.getTransaction().commit();
        } catch (PersistenceException pe) {
            throw new RuntimeException("Error deleting role", pe);
        }
    }

}
