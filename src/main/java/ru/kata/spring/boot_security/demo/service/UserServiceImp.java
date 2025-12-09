package ru.kata.spring.boot_security.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dto.AdminPasswordChangeDto;
import ru.kata.spring.boot_security.demo.dto.UserRegistrationDto;
import ru.kata.spring.boot_security.demo.dto.UserUpdateDto;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final Logger logger;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserServiceImp (UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public User getUserByIDWithRoles (long id) {
        logger.debug("Service: getUserByIDWithRoles id={id}", id);
        return userRepository.findByIdWithRoles(id).orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    @Transactional
    public User getUserByID (long id) {
        logger.debug("Service: finding user id={id}", id);
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    @Transactional
    public void checkExistsUserByIDOrThrow (Long id) {
        logger.debug("Service: check exist user in DB id={id}", id);
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
    }

    @Transactional
    public List <User> getAllUsers () {
        List <User> userList = userRepository.findAllWithRoles();
        logger.info("Service: Found {} users", userList.size());
        return userList;
    }

    @Transactional
    public void selfRegisterUser (UserRegistrationDto userDto) {
        User user = new User(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()));
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Роль ROLE_USER не найдена в БД!"));
        user.getSetOfRoles().add(userRole);

        logger.debug("Service: Try save user {}", user);

        User result = userRepository.save(user);

        logger.info("Service: User saved: {}", result);
    }

    @Transactional
    public void addUserForAdmin (UserRegistrationDto user) {
        logger.debug("Service: ADMIN: Try save user", user);

        User newUser = new User(user.getFirstName(), user.getLastName(), user.getEmail(), passwordEncoder.encode(user.getPassword()));
        List <Role> roles = roleRepository.findAllById(user.getRoleID());
        newUser.getSetOfRoles().addAll(roles);
        User result = userRepository.save(newUser);

        logger.info("Service: ADMIN: User saved: {}", result);
    }

    @Transactional
    public void updateUser (UserUpdateDto userDto) {
        logger.debug("Service: Try update user id {}", userDto.getId());

        User user = getUserByIDWithRoles(userDto.getId());
        user.setSetOfRoles(roleRepository.findAllById(userDto.getRoleID()).stream().collect(Collectors.toSet()));
        user.updateData(userDto);
        userRepository.save(user);

        logger.info("Service: User updated: {}", user);
    }

    @Transactional
    public void deleteUser (long id) {
        checkExistsUserByIDOrThrow(id);
        userRepository.deleteById(id);
        logger.info("Service: User with ID={} deleted", id);
    }

    @Transactional
    public boolean existsEmail (String email) {
        Optional <User> user = userRepository.findByEmail(email);

        logger.debug("Service: User with email exists - {}", user.isPresent());
        return user.isPresent();
    }


    @Transactional
    public void changePassword (AdminPasswordChangeDto passwordDto) {
        logger.debug("Service: Try update password for user id={}", passwordDto.getId());

        User user = userRepository.findById(passwordDto.getId()).orElseThrow(() -> new EntityNotFoundException("User with id " + passwordDto.getId() + " not found"));
        user.setPassword(passwordEncoder.encode(passwordDto.getPassword()));
        logger.info("Service: User password changed for user id={}", user.getId());
    }
}
