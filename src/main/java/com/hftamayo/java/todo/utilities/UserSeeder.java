package com.hftamayo.java.todo.utilities;

import com.hftamayo.java.todo.model.ERole;
import com.hftamayo.java.todo.model.Roles;
import com.hftamayo.java.todo.model.User;
import com.hftamayo.java.todo.repository.RolesRepository;
import com.hftamayo.java.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

@Component
public class UserSeeder implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${seed.development}")
    private boolean seedDevelopment;

    @Value("${seed.production}")
    private boolean seedProduction;

    private Roles adminRole;
    private Roles supervisorRole;
    private Roles userRole;

    public UserSeeder(UserRepository userRepository, RolesRepository rolesRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
    }

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
        }
    }

    private void setRoles() {
        System.out.println("Seeding roles started");
        userRole = new Roles(1, ERole.ROLE_USER, "User role", true, LocalDateTime.now(), LocalDateTime.now());
        rolesRepository.save(userRole);

        supervisorRole = new Roles(2, ERole.ROLE_SUPERVISOR, "Supervisor role", true, LocalDateTime.now(), LocalDateTime.now());
        rolesRepository.save(supervisorRole);

        adminRole = new Roles(3, ERole.ROLE_ADMIN, "Admin role", true, LocalDateTime.now(), LocalDateTime.now());
        rolesRepository.save(adminRole);

        System.out.println("Seeding roles completed");

    }

    private void seedDevelopment() {
        System.out.println("Seeding data for development environment started");
        User adminUser = new User(1L, "Herbert Tamayo", "hftamayo@gmail.com",
                passwordEncoder.encode("password123"), 25, true,
                true, true, true, true,
                LocalDateTime.now(), LocalDateTime.now(), Set.of(adminRole));
        userRepository.save(adminUser);

        User supervisorUser = new User(2L, "Sebastian Fernandez", "sebas@gmail.com",
                passwordEncoder.encode("password123"), 20, false,
                true, true, true, true,
                LocalDateTime.now(), LocalDateTime.now(), Set.of(supervisorRole));
        userRepository.save(supervisorUser);

        User operatorUser = new User(3L, "Milu Martinez", "milu@gmail.com",
                passwordEncoder.encode("password123"), 18, false,
                true, true, true, true,
                LocalDateTime.now(), LocalDateTime.now(), Set.of(userRole));
        userRepository.save(operatorUser);

        System.out.println("Seeding data for development environment completed");
    }

    private void seedProduction() {
        System.out.println("Seeding data for production environment started");
        User adminUser = new User(1L, "Administrator", "administrator@gmail.com",
                passwordEncoder.encode("password123"), 25, true,
                true, true, true, true,
                LocalDateTime.now(), LocalDateTime.now(), Set.of(adminRole));

        userRepository.save(adminUser);
        System.out.println("Seeding data for production environment completed");

    }
}
