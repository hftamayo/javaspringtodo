package com.hftamayo.java.todo.dao;

import com.hftamayo.java.todo.model.ERole;
import com.hftamayo.java.todo.model.Roles;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RolesDao {

    @Autowired
    public SessionFactory sessionFactory;

    public List<Roles> getRoles() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from " + Roles.class.getName(), Roles.class).list();
        } catch (HibernateException he) {
            throw new RuntimeException("Error retrieving roles", he);
        }
    }

    public Optional<Roles> getRoleByEnum(String roleEnum) {
        try (Session session = sessionFactory.openSession()) {
            ERole eRole = ERole.valueOf(roleEnum);
            Roles role = session.createQuery("from " + Roles.class.getName() + "where "
                            + ERole.class.getName() + " = :roleEnum", Roles.class)
                    .setParameter("roleEnum", eRole)
                    .uniqueResult();
            return Optional.ofNullable(role);
        } catch (HibernateException he) {
            throw new RuntimeException("Error retrieving roles", he);
        }
    }

    public Optional<Roles> findById(long roleId) {
        try (Session session = sessionFactory.openSession()) {
            Roles role = session.get(Roles.class, roleId);
            return Optional.ofNullable(role);
        } catch (HibernateException he) {
            throw new RuntimeException("Error retrieving roles", he);
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
