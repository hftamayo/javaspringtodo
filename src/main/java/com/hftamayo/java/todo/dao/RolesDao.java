package com.hftamayo.java.todo.dao;

import com.hftamayo.java.todo.model.ERole;
import com.hftamayo.java.todo.model.Roles;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RolesDao {

    @PersistenceContext
    public EntityManager entityManager;

    public List<Roles> getRoles() {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Roles> query = builder.createQuery(Roles.class);
            Root<Roles> root = query.from(Roles.class);
            query.select(root).orderBy(builder.desc(root.get("id")));

            return entityManager.createQuery(query).getResultList();

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

    public Optional<Roles> findById(long roleId) {
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

    public Roles saveRole(Roles newRole) {
        try {
            entityManager.getTransaction().begin();
            Roles existingRole = entityManager.find(Roles.class, newRole.getId());
            if (existingRole == null) {
                entityManager.persist(newRole);
            } else {
                newRole = entityManager.merge(newRole);
            }
            entityManager.getTransaction().commit();
            return newRole;
        } catch (PersistenceException pe) {
            throw new RuntimeException("Error saving role", pe);
        }
    }

    public Roles updateRole(long roleId, Roles updatedRole) {
        try {
            entityManager.getTransaction().begin();
            Roles existingRole = entityManager.find(Roles.class, roleId);
            if (existingRole != null) {
                existingRole.setRoleEnum(updatedRole.getRoleEnum());
                existingRole.setDescription(updatedRole.getDescription());
                existingRole.setStatus(updatedRole.isStatus());
                existingRole = entityManager.merge(existingRole);
            }
            entityManager.getTransaction().commit();
            return existingRole;
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
