package ru.kata.spring.boot_security.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.dto.UserRegistrationDto;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping ("/register")
public class RegistrationController {
    private final UserService userService;
    private final Logger logger;

    public RegistrationController (UserService userService) {
        this.userService = userService;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @GetMapping
    public String registerForm (Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }

    @PostMapping
    public String processRegistration (@ModelAttribute ("user") @Valid UserRegistrationDto user, BindingResult bindingResult) {
        logger.debug("Try add user to DB {}", user);
        if (bindingResult.hasErrors()) {
            logger.debug("Validation errors found, User {} not saved", user);
            return "register";
        }
        if (userService.existsEmail(user.getEmail())) {
            logger.debug("User with email {} already exists. User {} not saved", user.getEmail(), user);
            bindingResult.addError(new FieldError("user", "email", user.getEmail(), false, null, null,
                    "This e-mail already exists"));
            return "register";
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            logger.debug("Error confirm password. User {} not saved", user);
            bindingResult.addError(new FieldError("user", "password", user.getPassword(), false, null, null,
                    "Confirm password does not match"));
            return "register";
        }
        userService.selfRegisterUser(user);
        logger.info("User saved: {}. Redirect to /login", user);
        return "redirect:/login";
    }
}
