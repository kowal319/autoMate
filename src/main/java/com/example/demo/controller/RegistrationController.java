package com.example.demo.controller;

import com.example.demo.dto.RegistrationDTO;
import com.example.demo.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RegistrationController {

    private final CustomerService customerService;

    @Autowired
    public RegistrationController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationDTO", new RegistrationDTO());
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("registrationDTO") @Valid RegistrationDTO registrationDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "registration";
        }

        customerService.registerCustomer(registrationDTO);
        return "redirect:/login?registrationSuccess";
    }

    @GetMapping("/registration-success")
    public String registrationSuccess() {
        return "registrationSuccess";
    }


    @GetMapping("/login")
    public String shoHome(){
        return "loginPage";
    }

    @PostMapping("/login")
    public String login() {
         return "redirect:/profileUser"; // Redirect to the products page after successful login
    }
}

