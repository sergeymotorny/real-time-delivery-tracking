package com.motorny.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/adm")
public class AdmController {

    @GetMapping("/profile")
    public String getAdminProfile() {
        return "adm/profile";
    }
}
