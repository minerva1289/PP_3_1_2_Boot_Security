package ru.kata.spring.boot_security.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.AdminPasswordChangeDto;
import ru.kata.spring.boot_security.demo.dto.UserRegistrationDto;
import ru.kata.spring.boot_security.demo.dto.UserUpdateDto;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping ("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private final Logger logger;
    private final RoleRepository roleRepository;

    public AdminController (UserService userService, RoleService roleService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleService = roleService;
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.roleRepository = roleRepository;
    }

    @GetMapping
    @PreAuthorize ("hasRole('ADMIN')")
    public String showAdminPage (Model model) {
        logger.debug("GET: showAdminPage");

        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("list_users", userService.getAllUsers());
        return "users_list";
    }

    @GetMapping (value = "/new")
    @PreAuthorize ("hasRole('ADMIN')")
    public String showNewForm (@ModelAttribute ("regUser") UserRegistrationDto registrationUser, Model model) {
        logger.debug("GET: showNewForm");

        model.addAttribute("roles", roleService.findAll());
        return "new_user";
    }

    @PostMapping (value = "/new")
    @PreAuthorize ("hasRole('ADMIN')")
    public String addUser (@ModelAttribute ("regUser") @Valid UserRegistrationDto user, BindingResult bindingResult, Model model) {
        logger.debug("POST: Try add user to DB {}", user);
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.findAll());

            logger.debug("Validation errors found, User {} not saved", user);
            return "new_user";
        }
        if (userService.existsEmail(user.getEmail())) {
            logger.debug("User with email {} already exists. User {} not saved", user.getEmail(), user);

            bindingResult.addError(new FieldError("user", "email", user.getEmail(), false, null, null,
                    "This e-mail already exists"));
            model.addAttribute("roles", roleService.findAll());
            return "new_user";
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            logger.debug("Error confirm password. User {} not saved", user);

            bindingResult.addError(new FieldError("user", "confirmPassword", user.getConfirmPassword(), false, null, null,
                    "Confirm password does not match"));
            model.addAttribute("roles", roleService.findAll());
            return "new_user";
        }
        userService.addUserForAdmin(user);
        logger.info("User saved: email {}. Redirect to /admin", user.getEmail());
        return "redirect:/admin";
    }

    @GetMapping (value = "/edit")
    @PreAuthorize ("hasRole('ADMIN')")
    public String showEditForm (@RequestParam long id, Model model) {
        logger.debug("GET: Show edit form for user with id {}", id);

        model.addAttribute("user", new UserUpdateDto(userService.getUserByIDWithRoles(id)));
        model.addAttribute("list_roles", roleService.findAll());

        logger.info("Found user with id {}. Show edit form", id);
        return "user_edit";
    }

    @PostMapping (value = "/edit")
    @PreAuthorize ("hasRole('ADMIN')")
    public String editUser (@ModelAttribute ("user") @Valid UserUpdateDto userDto, BindingResult bindingResult, Model model) {
        logger.debug("Try to edit user with id {}", userDto.getId());
        userService.checkExistsUserByIDOrThrow(userDto.getId());
        if (bindingResult.hasErrors()) {
            model.addAttribute("list_roles", roleService.findAll());

            logger.debug("Validation errors found, User {} not updated", userDto);
            return "user_edit";
        }
        if (!userService.getUserByID(userDto.getId()).getEmail().equals(userDto.getEmail()) && userService.existsEmail(userDto.getEmail())) {
            logger.debug("User with email {} already exists. User {} not updated", userDto.getEmail(), userDto);

            bindingResult.addError(new FieldError("user", "email", userDto.getEmail(), false, null, null,
                    "This e-mail already exists"));
            model.addAttribute("list_roles", roleService.findAll());
            return "user_edit";
        }
        userService.updateUser(userDto);
        logger.info("User updated: {}. Redirect to /admin", userDto);
        return "redirect:/admin";
    }

    @GetMapping (value = "/edit/change-password")
    @PreAuthorize ("hasRole('ADMIN')")
    public String changePassword (@RequestParam long id, Model model) {
        logger.debug("Show form for change password for user with id {}", id);

        userService.checkExistsUserByIDOrThrow(id);
        model.addAttribute("adminPasswordChangeDto", new AdminPasswordChangeDto(id));
        return "user_change_password";
    }

    @PostMapping (value = "/edit/change-password")
    @PreAuthorize ("hasRole('ADMIN')")
    public String savePassword (@Valid AdminPasswordChangeDto adminPasswordChangeDto, BindingResult bindingResult, Model model) {
        logger.debug("Change password for user with id {}", adminPasswordChangeDto.getId());
        userService.checkExistsUserByIDOrThrow(adminPasswordChangeDto.getId());
        if (bindingResult.hasErrors()) {
            logger.debug("Validation errors found, password not change");

            return "user_change_password";
        }
        if (!adminPasswordChangeDto.getPassword().equals(adminPasswordChangeDto.getConfirmPassword())) {
            logger.debug("Error confirm password");

            bindingResult.addError(new FieldError("userDto", "confirmPassword", adminPasswordChangeDto.getConfirmPassword(), false, null, null,
                    "Confirm password does not match"));
            return "user_change_password";
        }
        userService.changePassword(adminPasswordChangeDto);
        logger.info("Password changed for user with id {}. Redirect to /admin", adminPasswordChangeDto.getId());
        return "redirect:/admin";
    }

    @PostMapping (value = "/delete")
    @PreAuthorize ("hasRole('ADMIN')")
    public String deleteUser (@RequestParam long id) {
        logger.debug("Try delete user with id {}", id);
        userService.deleteUser(id);
        logger.info("User with id {} deleted. Redirect to /admin", id);
        return "redirect:/admin";
    }
}
