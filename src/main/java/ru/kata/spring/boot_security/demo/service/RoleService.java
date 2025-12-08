package ru.kata.spring.boot_security.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
   // private final PasswordEncoder passwordEncoder;
    //private  final UserRepository userRepository;
    private final Logger logger;

    public RoleService(RoleRepository roleRepository/*, PasswordEncoder passwordEncoder, UserRepository userRepository*/) {
        this.roleRepository = roleRepository;
       // this.passwordEncoder = passwordEncoder;
       // this.userRepository = userRepository;
        logger = LoggerFactory.getLogger(RoleService.class);
    }
    //переделать в сервисе юзер и контроллере и инициализаторе
    public Role findByName(String roleName) {
        return roleRepository.findByName(roleName).orElse(null);
    }
    public Role findById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }
    public Role save(Role role) {
        return roleRepository.save(role);
    }
    public List <Role> findAll() {
        return roleRepository.findAll();
    }


}
