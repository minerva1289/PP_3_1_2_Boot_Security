package ru.kata.spring.boot_security.demo.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

@Component
public class DataInitializer implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private final UserService userService;
    private final RoleService roleService;

    public  DataInitializer(PasswordEncoder passwordEncoder, UserService userService, RoleService roleService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    @Transactional
    public void run (String... args) throws Exception {

        if (!roleService.findByName("USER").isPresent()) {
            roleService.save(new Role("USER"));
            log.info("Роль USER добавлена.");
        }
        if (!roleService.findByName("ADMIN").isPresent()) {
            Role adminRole = new Role("ADMIN");
            roleService.save(adminRole);
            log.info("Роль ADMIN добавлена.");
            User adminUser = new User("admin", "qwerty", "admin", passwordEncoder.encode("admin"));
            adminUser.getSetOfRoles().add(adminRole);
            userService.saveUser(adminUser);
            log.info("User admin, password admin добавлен");
        }
    }
}
