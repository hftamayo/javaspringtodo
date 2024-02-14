package com.hftamayo.java.todo.Services;

import com.hftamayo.java.todo.Model.Roles;
import com.hftamayo.java.todo.Model.User;
import com.hftamayo.java.todo.Repository.RolesRepository;
import com.hftamayo.java.todo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserSeeder implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

@Value("${seed.development}")
    private boolean seedDevelopment;

@Value("${seed.production}")
    private boolean seedProduction;

    public UserSeeder(UserRepository userRepository, RolesRepository rolesRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        if (seedDevelopment) {
            seedDevelopment();
        }
        if (seedProduction) {
            seedProduction();
        }
    }

    private void seedDevelopment() {
        Roles adminRole = new Roles(1, "ADMIN", "Admin Role", true,
                LocalDateTime.now(), LocalDateTime.now());
        Roles supervisorRole = new Roles(2, "SUPERVISOR", "Supervisor Role", true,
                LocalDateTime.now(), LocalDateTime.now());
        Roles operatorRole = new Roles(3, "OPERATOR", "Operator Role", true,
                LocalDateTime.now(), LocalDateTime.now());

        rolesRepository.save(adminRole);
        rolesRepository.save(supervisorRole);
        rolesRepository.save(operatorRole);

        User adminUser = new User(1, "administrator", "administrator@gmail.com",
                passwordEncoder.encode("password123"), 25, true, true, LocalDateTime.now(), LocalDateTime.now());
        adminUser.setRoles(adminRole);
        userRepository.save(adminUser);

        User supervisorUser = new User();


    }

    private void seedProduction() {
        System.out.println("Seeding Production");
    }
}
