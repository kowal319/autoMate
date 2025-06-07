package com.example.demo.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeAdminViewController {

    @GetMapping("/admin/home")
    public String showAdminHomePage() {
        return "admin/homeAdmin";
    }
}