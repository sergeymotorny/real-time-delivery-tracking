package com.motorny.controllers;

import com.motorny.dto.user.UserAuthDto;
import com.motorny.dto.user.UserCreateDto;
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
        model.addAttribute("userCreateDto", new UserCreateDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userCreateDto") UserCreateDto userCreateDto, BindingResult result) {
        if (result.hasErrors())
            return "auth/register";

        userService.createUser(userCreateDto);
        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("userDto", new UserAuthDto());
        return "auth/login";
    }

    @PostMapping("/login")
    public String processLogin(@Valid @RequestParam String email, BindingResult result) {
        if (result.hasErrors())
            return "auth/login";

        userService.getUserByEmail(email);
        return "redirect:/user/profile";
    }
}
