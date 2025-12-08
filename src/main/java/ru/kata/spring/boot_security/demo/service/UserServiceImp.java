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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
    public class UserServiceImp implements UserService {

        private final UserRepository userRepository;
        private final Logger logger;
        private final PasswordEncoder passwordEncoder;
        //private final UserDetailsService userDetailsService;
        private final RoleRepository roleRepository;

        public UserServiceImp (UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
            this.userRepository = userRepository;
            this.logger = LoggerFactory.getLogger(this.getClass());
            this.passwordEncoder = passwordEncoder;
            this.roleRepository = roleRepository;
        }

        @Transactional
        public User getUserByIDWithRoles (long id) {
            return userRepository.findByIdWithRoles(id).orElse(null);
        }

        @Transactional
        public User getUserByID (long id) {
            return userRepository.findById(id).orElse(null);
        }

        @Transactional
        public List <User> getAllUsers () {
//            logger.info("Get all users started");
//            Iterable <User> users = userRepository.findAll();
//            logger.info("Get all users after findall");
//            List <User> userList = StreamSupport.stream(users.spliterator(), false).collect(Collectors.toList());
            List<User> userList = userRepository.findAllWithRoles();
            logger.info("Found {} users", userList.size());
            return userList;
        }

        @Transactional
        public void selfRegisterUser (UserRegistrationDto userDto) {
            User user = new User(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()));
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Роль ROLE_USER не найдена в БД!"));
            user.getSetOfRoles().add(userRole);

            logger.debug("Try save user {}", user);

            User result = userRepository.save(user);

            logger.info("User saved: {}", result);
        }
        @Transactional
        public void addUserForAdmin (UserRegistrationDto user) {
            logger.debug("ADMIN: Try save user", user);

            User newUser = new User(user.getFirstName(), user.getLastName(), user.getEmail(), passwordEncoder.encode(user.getPassword()));
            List<Role> roles = roleRepository.findAllById(user.getRoleID());
            newUser.getSetOfRoles().addAll(roles);
            User result = userRepository.save(newUser);

            logger.info("ADMIN: User saved: {}", result);
        }

        @Transactional
        public void updateUser (UserUpdateDto userDto) {
            logger.debug("Try update user id {}", userDto.getId());
            User user = getUserByIDWithRoles(userDto.getId());
            //Set <Role> userRoles = user.getSetOfRoles();
           // Set<Long> userDtoRoles = userDto.getRoleID();
//            if(!userDto.getNewPassword().isEmpty()) {
//                user.setPassword(passwordEncoder.encode(userDto.getNewPassword()));
//            }
            user.setSetOfRoles(roleRepository.findAllById(userDto.getRoleID()).stream().collect(Collectors.toSet()));
            user.updateData(userDto);
            userRepository.save(user);
            logger.info("User updated: {}", user);
        }

        @Transactional
        public void deleteUser (long id) {
            userRepository.deleteById(id);
            logger.info("User with ID={} deleted", id);
        }

        @Transactional
        public boolean existsEmail (String email) {
            logger.debug("Check if user with email {} exists", email);
            Optional <User> user = userRepository.findByEmail(email);
            logger.debug("User with email exists - {}", user.isPresent());
            return user.isPresent();
        }

        @Transactional
        public User getUserByEmail (String email) {
            return userRepository.findByEmail(email).orElse(null);
        }

        @Transactional
        public void changePassword (AdminPasswordChangeDto passwordDto) {
            User user = userRepository.findById(passwordDto.getId()).orElse(null);
            if (user != null) {
                user.setPassword(passwordEncoder.encode(passwordDto.getPassword()));
            }
        }
}
