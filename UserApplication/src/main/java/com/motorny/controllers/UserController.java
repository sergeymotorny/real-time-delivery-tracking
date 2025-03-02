package com.motorny.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/users")
public class UserController {


    @GetMapping("/home")
    public String showHome() {
        return "user/home";
    }

    @GetMapping("/profile")
    public String showProfile() {
        return "user/profile";
    }



}
