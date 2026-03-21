package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

//    @GetMapping("/home")
//    public String home(Model model, Authentication authentication) {
//        String email = authentication.getName();
//        model.addAttribute("email", email);
//        return "home";
//    }
@GetMapping("/home")
public String home(Authentication authentication) {
    if (authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
        return "redirect:/admin";
    } else if (authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"))) {
        return "redirect:/manager";
    } else {
        return "home"; // employee default
    }
}
}