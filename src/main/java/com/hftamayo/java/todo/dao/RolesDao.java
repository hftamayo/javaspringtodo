package com.hftamayo.java.todo.dao;

import com.hftamayo.java.todo.model.ERole;
import com.hftamayo.java.todo.model.Roles;
import org.hibernate.HibernateException;
import org.hibernate.Session;

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
    try (Session session = sessionFactory.openSession()) {
        session.beginTransaction();
        session.persist(newRole);
        session.getTransaction().commit();
        return newRole;
    } catch (HibernateException he) {
        throw new RuntimeException("Error retrieving roles", he);
    }
}

public Roles updateRole(long roleId, Roles updatedRole) {
    try (Session session = sessionFactory.openSession()) {
        session.beginTransaction();
        Roles role = session.get(Roles.class, roleId);
        role.setRoleEnum(updatedRole.getRoleEnum());
        role.setDescription(updatedRole.getDescription());
        role.setStatus(updatedRole.isStatus());
        Roles mergedRole = (Roles) session.merge(role);
        session.getTransaction().commit();
        return mergedRole;
    } catch (HibernateException he) {
        throw new RuntimeException("Error retrieving roles", he);
    }
}

public void deleteRole(long roleId) {
    try (Session session = sessionFactory.openSession()) {
        session.beginTransaction();
        Roles role = session.get(Roles.class, roleId);
        session.remove(role);
        session.getTransaction().commit();
    } catch (HibernateException he) {
        throw new RuntimeException("Error retrieving roles", he);
    }
}

}
