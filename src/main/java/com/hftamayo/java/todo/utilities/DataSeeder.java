package com.hftamayo.java.todo.utilities;

import com.hftamayo.java.todo.entity.ERole;
import com.hftamayo.java.todo.entity.Roles;
import com.hftamayo.java.todo.entity.User;
import com.hftamayo.java.todo.services.RolesService;
import com.hftamayo.java.todo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;

/*
this class only requires integration testing
 */

@Component
@RequiredArgsConstructor
public class DataSeeder implements ApplicationListener<ApplicationReadyEvent> {
    private final UserService userService;
    private final RolesService rolesService;

    @Value("${seed.development}")
    private boolean seedDevelopment;

    @Value("${seed.production}")
    private boolean seedProduction;

    @Value("${seed.admin.password}")
    private String adminPasswod;

    @Value("${seed.supervisor.password}")
    private String supervisorPassword;

    @Value("${seed.user1.password}")
    private String user1Password;

    @Value("${seed.user2.password}")
    private String user2Password;

    private Roles adminRole;
    private Roles supervisorRole;
    private Roles userRole;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        try {
            if (seedDevelopment || seedProduction) {
                setRoles();
            } else {
                System.out.println("No data seeding required for Roles");
            }

            if (seedDevelopment) {
                seedDevelopment();
            } else {
                System.out.println("No data seeding required for Users" +
                        ", Data already seeded in development enviro");

            }
            if (seedProduction) {
                seedProduction();
            } else {
                System.out.println("No data seeding required for Users" +
                        ", Data already seeded in production enviro");
            }

        } catch (Exception e) {
            System.out.println("Error seeding data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setRoles() {
        System.out.println("Seeding roles started");

        userRole = new Roles(null, ERole.ROLE_USER, "User role", true,
                LocalDateTime.now(), LocalDateTime.now(), new HashSet<>());
        rolesService.saveRole(userRole);

        supervisorRole = new Roles(null, ERole.ROLE_SUPERVISOR, "Supervisor role", true,
                LocalDateTime.now(), LocalDateTime.now(), new HashSet<>());
        rolesService.saveRole(supervisorRole);

        adminRole = new Roles(null, ERole.ROLE_ADMIN, "Admin role", true,
                LocalDateTime.now(), LocalDateTime.now(), new HashSet<>());
        rolesService.saveRole(adminRole);

        System.out.println("Seeding roles completed");
    }

    private void seedDevelopment() {
        System.out.println("Seeding user data for development environment started");

        User adminUser = new User(null, "Herbert Tamayo", "administrador@tamayo.com",
                adminPasswod.trim(), 25, true,
                true, true, true, true,
                LocalDateTime.now(), LocalDateTime.now(), adminRole, new HashSet<>());
        userService.saveUser(adminUser);

        User supervisorUser = new User(null, "Sebastian Fernandez", "supervisor@tamayo.com",
                supervisorPassword.trim(), 20, false,
                true, true, true, true,
                LocalDateTime.now(), LocalDateTime.now(), supervisorRole, new HashSet<>());
        userService.saveUser(supervisorUser);

        User operatorUser = new User(null, "Bob Doe", "bob@tamayo.com",
                user1Password.trim(), 18, false,
                true, true, true, true,
                LocalDateTime.now(), LocalDateTime.now(), userRole, new HashSet<>());
        userService.saveUser(operatorUser);

        operatorUser = new User(null, "Mary Doe", "mary@tamayo.com",
                user2Password.trim(), 18, false,
                true, true, true, true,
                LocalDateTime.now(), LocalDateTime.now(), userRole, new HashSet<>());
        userService.saveUser(operatorUser);
        System.out.println("Seeding data for development environment completed");
    }

    private void seedProduction() {
        System.out.println("Seeding user data for production environment started");
        User adminUser = new User(null, "Administrator", "administrador@tamayo.com",
                adminPasswod.trim(), 25, true,
                true, true, true, true,
                LocalDateTime.now(), LocalDateTime.now(), adminRole, new HashSet<>());

        userService.saveUser(adminUser);
        System.out.println("Seeding data for production environment completed");

    }
}
