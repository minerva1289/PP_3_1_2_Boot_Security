package ru.kata.spring.boot_security.demo.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

@Component
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    public DataInitializer(RoleRepository roleRepository, UserService userService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (!roleRepository.findByName("ROLE_USER").isPresent()) {
            roleRepository.save(new Role("ROLE_USER"));
            log.info("Роль ROLE_USER добавлена.");
        }
        if (!roleRepository.findByName("ROLE_ADMIN").isPresent()) {
           Role adminRole = new Role("ROLE_ADMIN");
            roleRepository.save(adminRole);
            log.info("Роль ADMIN_USER добавлена.");
            User adminUser = new User("admin", "qwerty", "admin" , passwordEncoder.encode("admin"));
            adminUser.getSetOfRoles().add(adminRole);
            userRepository.save(adminUser);
            log.info("User admin, password admin добавлен");
        }
    }
}
