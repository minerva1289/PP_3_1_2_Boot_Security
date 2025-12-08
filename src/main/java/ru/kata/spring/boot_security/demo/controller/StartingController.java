package ru.kata.spring.boot_security.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
public class   StartingController {

    private UserService userService;;
    private final Logger logger;

    public StartingController(UserService userService) {

        this.userService = userService;
        this.logger = LoggerFactory.getLogger(StartingController.class);
    }

    @GetMapping(value = "/user")
    public String showUser (@AuthenticationPrincipal User user, @RequestParam long id, Model model) {
        if (user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            System.out.println("ROLE_ADMIN HAVE!!!!!");
            model.addAttribute("user", userService.getUserByIDWithRoles(id));
            return "user";
        }
        else if (Long.compare(user.getId(), id) != 0) {
            return  "error";
        }
        model.addAttribute("user", user);
        return "user";
    }
}
