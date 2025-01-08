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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;

/*
this class only requires integration testing
 */

@Component
@RequiredArgsConstructor
public class UserSeeder implements ApplicationListener<ApplicationReadyEvent> {
    private final UserService userService;
    private final RolesService rolesService;
    private final PasswordEncoder passwordEncoder;

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
                System.out.println("No data seeding required on Development environment");

            }
            if (seedProduction) {
                seedProduction();
            } else {
                System.out.println("No data seeding required on Production environment");
            }

        } catch (Exception e) {
            System.out.println("Error seeding data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setRoles() {
        System.out.println("Seeding roles started");

        userRole = new Roles(1, ERole.ROLE_USER, "User role", true,
                LocalDateTime.now(), LocalDateTime.now(), new HashSet<>());
        rolesService.saveRole(userRole);

        supervisorRole = new Roles(2, ERole.ROLE_SUPERVISOR, "Supervisor role", true,
                LocalDateTime.now(), LocalDateTime.now(), new HashSet<>());
        rolesService.saveRole(supervisorRole);

        adminRole = new Roles(3, ERole.ROLE_ADMIN, "Admin role", true,
                LocalDateTime.now(), LocalDateTime.now(), new HashSet<>());
        rolesService.saveRole(adminRole);

        System.out.println("Seeding roles completed");
    }

    private void seedDevelopment() {
        System.out.println("Seeding data for development environment started");
        User adminUser = new User(1L, "Herbert Tamayo", "administrador@tamayo.com",
                passwordEncoder.encode(adminPasswod), 25, true,
                true, true, true, true,
                LocalDateTime.now(), LocalDateTime.now(), adminRole, new HashSet<>());
        userService.saveUser(adminUser);

        User supervisorUser = new User(2L, "Sebastian Fernandez", "supervisor@tamayo.com",
                passwordEncoder.encode(supervisorPassword), 20, false,
                true, true, true, true,
                LocalDateTime.now(), LocalDateTime.now(), supervisorRole, new HashSet<>());
        userService.saveUser(supervisorUser);

        User operatorUser = new User(3L, "Bob Doe", "bob@tamayo.com",
                passwordEncoder.encode(user1Password), 18, false,
                true, true, true, true,
                LocalDateTime.now(), LocalDateTime.now(), userRole, new HashSet<>());
        userService.saveUser(operatorUser);

        operatorUser = new User(4L, "Mary Doe", "mary@tamayo.com",
                passwordEncoder.encode(user2Password), 18, false,
                true, true, true, true,
                LocalDateTime.now(), LocalDateTime.now(), userRole, new HashSet<>());
        userService.saveUser(operatorUser);

        System.out.println("Seeding data for development environment completed");

        System.out.println("Starting password salting verification");
        String rawPassword = "milucito3";
        String storedHash = "$2a$10$ZWeqtp.sCj4qIJWCf8tnnOs/f.268iKeTICNLdGLz9owOUMNzVdUu";

        boolean matches = passwordEncoder.matches(rawPassword, storedHash);
        System.out.println("Password matches: " + matches);

        System.out.println("Password salting verification completed");
    }

    private void seedProduction() {
        System.out.println("Seeding data for production environment started");
        User adminUser = new User(1L, "Administrator", "administrador@tamayo.com",
                passwordEncoder.encode(adminPasswod), 25, true,
                true, true, true, true,
                LocalDateTime.now(), LocalDateTime.now(), adminRole, new HashSet<>());

        userService.saveUser(adminUser);
        System.out.println("Seeding data for production environment completed");

    }
}
