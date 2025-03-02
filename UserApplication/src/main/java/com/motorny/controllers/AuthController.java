package com.motorny.controllers;

import com.motorny.models.User;
import com.motorny.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute User user, BindingResult result) {
        if (result.hasErrors())
            return "auth/register";

        userService.createUser(user);
        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String showLoginForm(@Valid @ModelAttribute("user") User user) {
        return "auth/login";
    }

    @PostMapping("/login")
    public String processLogin(@Valid @RequestParam String email, BindingResult result) {
        if (result.hasErrors())
            return "auth/login";

        userService.findByEmail(email);
        return "redirect:/user/profile";
    }
}
