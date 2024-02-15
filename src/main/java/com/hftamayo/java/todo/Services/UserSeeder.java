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
    private Roles operatorRole;

    public UserSeeder(UserRepository userRepository, RolesRepository rolesRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        setRoles();

        if (seedDevelopment) {
            seedDevelopment();
        }
        if (seedProduction) {
            seedProduction();
        }
    }

    private void setRoles() {
        if (rolesRepository.count() == 0) {
            adminRole = new Roles(1, "ADMIN", "Admin Role", true,
                    LocalDateTime.now(), LocalDateTime.now());
            supervisorRole = new Roles(2, "SUPERVISOR", "Supervisor Role", true,
                    LocalDateTime.now(), LocalDateTime.now());
            operatorRole = new Roles(3, "OPERATOR", "Operator Role", true,
                    LocalDateTime.now(), LocalDateTime.now());

            rolesRepository.save(adminRole);
            rolesRepository.save(supervisorRole);
            rolesRepository.save(operatorRole);
        }
    }

    private void seedDevelopment() {
        User adminUser = new User(1, "Herbert Tamayo", "hftamayo@gmail.com",
                passwordEncoder.encode("password123"), 25, true,
                true, LocalDateTime.now(), LocalDateTime.now(), Set.of(adminRole));
        userRepository.save(adminUser);

        User supervisorUser = new User(2, "Sebastian Fernandez", "sebas@gmail.com",
                passwordEncoder.encode("password123"), 20, false,
                true, LocalDateTime.now(), LocalDateTime.now(), Set.of(supervisorRole));
        userRepository.save(supervisorUser);

        User operatorUser = new User(3, "Milu Martinez", "milu@gmail.com",
                passwordEncoder.encode("password123"), 20, false,
                true, LocalDateTime.now(), LocalDateTime.now(), Set.of(operatorRole));
        userRepository.save(operatorUser);
    }

    private void seedProduction() {
        User adminUser = new User(1, "Administrator", "administrator@localhost.com",
                passwordEncoder.encode("password123"), 25, true,
                true, LocalDateTime.now(), LocalDateTime.now(), Set.of(adminRole));
        userRepository.save(adminUser);

    }
}
