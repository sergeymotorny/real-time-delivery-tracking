package com.motorny.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String showHomePage() {
        return "home";
    }

    @GetMapping("/faqs")
    public String showFAQPage() {
        return "faqs";
    }

    @GetMapping("/abouts")
    public String showAboutPage() {
        return "about";
    }

    @GetMapping("/contacts")
    public String showContactPage() {
        return "contacts";
    }

    @GetMapping("/supports")
    public String showSupportPage() {
        return "support";
    }
}
