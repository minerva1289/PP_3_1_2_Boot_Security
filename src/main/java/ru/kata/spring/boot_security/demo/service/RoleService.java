package ru.kata.spring.boot_security.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final Logger logger;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        logger = LoggerFactory.getLogger(RoleService.class);
    }
    public Optional<Role> findByName(String roleName) {
        return roleRepository.findByName(roleName);
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
