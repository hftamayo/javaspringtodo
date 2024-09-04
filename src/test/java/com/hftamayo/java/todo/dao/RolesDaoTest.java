package com.hftamayo.java.todo.dao;

import com.hftamayo.java.todo.model.ERole;
import com.hftamayo.java.todo.model.Roles;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;

public class RolesDaoTest {
    private RolesDao rolesDao;
    private Roles role;
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        entityManager = Mockito.mock(EntityManager.class);
        rolesDao = Mockito.mock(RolesDao.class);
        role = Roles.builder()
                .id(1L)
                .roleEnum(ERole.ROLE_USER)
                .description("User role")
                .status(true)
                .build();
    }

    @Test
    public void testGetRoles() {
        Mockito.when(rolesDao.getRoles()).thenReturn(Collections.singletonList(role));
        List<Roles> rolesList = rolesDao.getRoles();
        Assertions.assertFalse(rolesList.isEmpty());
        Assertions.assertEquals(1, rolesList.size());
        Assertions.assertEquals(role, rolesList.get(0));
    }

    @Test
    public void testGetRoleById() {
        CriteriaBuilder builder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<Roles> query = Mockito.mock(CriteriaQuery.class);
        Root<Roles> root = Mockito.mock(Root.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(builder);
        Mockito.when(builder.createQuery(Roles.class)).thenReturn(query);
        Mockito.when(query.from(Roles.class)).thenReturn(root);
        Mockito.when(query.select(root)).thenReturn(query);
        Mockito.when(query.where(builder.equal(root.get("id"), 1L))).thenReturn(query);
        Mockito.when(entityManager.createQuery(query).getSingleResult()).thenReturn(role);

        Optional<Roles> foundRole = rolesDao.getRoleById(1L);
        Assertions.assertTrue(foundRole.isPresent());
        Assertions.assertEquals(role, foundRole.get());
    }

    @Test
    public void testGetRoleByEnum() {
        CriteriaBuilder builder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<Roles> query = Mockito.mock(CriteriaQuery.class);
        Root<Roles> root = Mockito.mock(Root.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(builder);
        Mockito.when(builder.createQuery(Roles.class)).thenReturn(query);
        Mockito.when(query.from(Roles.class)).thenReturn(root);
        Mockito.when(query.select(root)).thenReturn(query);
        Mockito.when(query.where(builder.equal(root.get("roleEnum"), ERole.ROLE_USER))).thenReturn(query);
        Mockito.when(entityManager.createQuery(query).getSingleResult()).thenReturn(role);

        Optional<Roles> foundRole = rolesDao.getRoleByEnum("ROLE_USER");
        Assertions.assertTrue(foundRole.isPresent());
        Assertions.assertEquals(role, foundRole.get());
    }

    @Test
    public void testSaveRole() {
        Mockito.when(rolesDao.saveRole(role)).thenReturn(role);
        Roles savedRole = rolesDao.saveRole(role);
        Assertions.assertEquals(role, savedRole);
    }

    @Test
    public void testUpdateRole() {
        Map<String, Object> propertiesToUpdate = new HashMap<>();
        propertiesToUpdate.put("description", "Updated User role");
        propertiesToUpdate.put("status", false);

        Mockito.when(entityManager.find(Roles.class, 1L)).thenReturn(role);
        Mockito.when(entityManager.merge(role)).thenReturn(role);

        Roles updatedRole = rolesDao.updateRole(1L, propertiesToUpdate);

        Assertions.assertEquals("Updated User role", updatedRole.getDescription());
        Assertions.assertFalse(updatedRole.isStatus());
    }

    @Test
    public void testDeleteRole() {
        Mockito.doNothing().when(rolesDao).deleteRole(1L);
        rolesDao.deleteRole(1L);
        Mockito.verify(rolesDao, Mockito.times(1)).deleteRole(1L);
    }

}
