package com.motorny.controllers;

import com.motorny.dto.UserDto;
import com.motorny.service.OrderService;
import com.motorny.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final OrderService orderService;

    @GetMapping("/profile")
    public String showProfile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("user", userService.getUserProfile(userDetails));
        return "user/profile";
    }

    @GetMapping("/orders")
    public String getAllOrdersByUser(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("orders", orderService.getOrdersByUser(userDetails.getUsername()));
        return "user/orders";
    }

    @GetMapping("/form-update/{id}")
    public String showUserUpdateForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "user/update_user";
    }

    @PostMapping("/update/{id}")
    public String updateUserById(@PathVariable("id") Long id, @Valid @ModelAttribute("user") UserDto userDto,
                                 BindingResult result) {
        if (result.hasErrors())
            return "user/update_user";

        userService.updateUser(id, userDto);
        return "redirect:/users/profile";
    }
}
