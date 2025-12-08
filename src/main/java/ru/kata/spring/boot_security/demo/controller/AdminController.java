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
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private final Logger logger;
    private final RoleRepository roleRepository;

    public AdminController(UserService userService, RoleService roleService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleService = roleService;
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.roleRepository = roleRepository;
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String showAdminPage(/*@AuthenticationPrincipal User user,*/ Model model) {
        //model.addAttribute("user", user);
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("list_users", userService.getAllUsers());
        logger.info("START showAdminPage");
        return "users_list";
    }

    @GetMapping (value = "/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String showNewForm (@ModelAttribute ("regUser") UserRegistrationDto registrationUser, Model model) {
        model.addAttribute("roles", roleService.findAll());
        return "new_user";
    }
    @PostMapping(value = "/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String addUser (@ModelAttribute ("regUser") @Valid UserRegistrationDto user, BindingResult bindingResult, Model model) {
        logger.debug("Try add user to DB {}", user);
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.findAll());
            logger.debug("Validation errors found, User {} not saved", user);
            return "new_user";
        }
        //error exist
        if (userService.existsEmail(user.getEmail())) {
            System.out.println(user.getRoleID());
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
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditForm (/*@AuthenticationPrincipal User user,*/ @RequestParam long id, Model model) {
        System.out.println("START!!!! showEditForm");
        logger.debug("Find user with id {}", id);
        if (userService.getUserByIDWithRoles(id) == null) {
            logger.info("User with id {} not found. Redirect to /admin", id);
            return "redirect:/admin";
        }
        model.addAttribute("user", new UserUpdateDto(userService.getUserByIDWithRoles(id)));
        //model.addAttribute("user", userService.getUserByID(id));
        model.addAttribute("list_roles", roleService.findAll());
        logger.info("Found user with id {}. Show edit form", id);
        return "user_edit";
    }
//добавить валидацию
    @PostMapping(value = "/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String editUser (@ModelAttribute ("userDto") UserUpdateDto userDto, @RequestParam long id, Model model) {

        logger.debug("Edit user with id {}", id);
        if (userService.getUserByIDWithRoles(id) == null) {
            logger.info("User with id {} not found. Redirect to /admin", id);
            return "redirect:/admin";
        }
        userService.updateUser(userDto);
        //userService.addUserForAdmin(user);
        logger.info("User updated: {}. Redirect to /admin", userDto);
        return "redirect:/admin";
    }
    @GetMapping(value = "/edit/change-password")
    @PreAuthorize("hasRole('ADMIN')")
    public String changePassword (/*@AuthenticationPrincipal User user,*/ @RequestParam long id, Model model) {
        //logger.debug("Change password for user with id {}", id);
        if (userService.getUserByIDWithRoles(id) == null) {
            logger.info("User with id {} not found. Redirect to /admin", id);
            return "redirect:/admin";
        }
        model.addAttribute("adminPasswordChangeDto", new AdminPasswordChangeDto(id));
        return "user_change_password";
    }

    @PostMapping(value = "/edit/change-password")
    @PreAuthorize("hasRole('ADMIN')")
    public String savePassword (/*@AuthenticationPrincipal User user,*/ @RequestParam long id, @Valid AdminPasswordChangeDto adminPasswordChangeDto, BindingResult bindingResult, Model model) {
        logger.debug("Change password for user with id {}", id);
        if (userService.getUserByIDWithRoles(id) == null) {
            logger.info("User with id {} not found. Redirect to /admin", id);
            return "redirect:/admin";
        }
        if (bindingResult.hasErrors()) {
            logger.debug("Validation errors found, password not change");
            return "user_change_password";
            //return "redirect:/admin";
        }
        if (!adminPasswordChangeDto.getPassword().equals(adminPasswordChangeDto.getConfirmPassword())) {
            logger.debug("Error confirm password");
            bindingResult.addError(new FieldError("userDto", "confirmPassword", adminPasswordChangeDto.getConfirmPassword(), false, null, null,
                    "Confirm password does not match"));
            //System.out.println("ID" + adminPasswordChangeDto.getId());
            return "user_change_password";
            //return "redirect:/admin";

        }
        userService.changePassword(adminPasswordChangeDto);
        return "redirect:/admin";
    }

    @PostMapping (value = "/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser (@RequestParam long id) {
        logger.debug("Try delete user with id {}", id);
        if (userService.getUserByID(id) == null) {
            logger.info("User with id {} not found. Can't delete", id);
            return "redirect:/admin";
        }
        userService.deleteUser(id);
        logger.debug("User with id {} deleted. Redirect to /", id);
        return "redirect:/admin";
    }
}
