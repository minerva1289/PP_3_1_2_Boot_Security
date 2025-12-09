package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.dto.AdminPasswordChangeDto;
import ru.kata.spring.boot_security.demo.dto.UserRegistrationDto;
import ru.kata.spring.boot_security.demo.dto.UserUpdateDto;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    User getUserByIDWithRoles (long id);

    List <User> getAllUsers ();

    void selfRegisterUser (UserRegistrationDto user);

    void addUserForAdmin (UserRegistrationDto user);

    void deleteUser (long id);

    boolean existsEmail (String email);

    void updateUser (UserUpdateDto userDto);

    void changePassword (AdminPasswordChangeDto adminPasswordChangeDto);

    User getUserByID (long id);

    void checkExistsUserByIDOrThrow (Long id);
}
