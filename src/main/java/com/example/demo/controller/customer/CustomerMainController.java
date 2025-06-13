package com.example.demo.controller.customer;

import com.example.demo.entity.Customer;
import com.example.demo.service.CustomerService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/profileUser")
public class CustomerMainController {

    private final CustomerService customerService;

    public CustomerMainController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @GetMapping
    public String showCustomerHome(Model model, Authentication authentication) {
        String loggedCustomer = authentication.getName();
        Optional<Customer> currentCustomer = customerService.findByEmail(loggedCustomer);
         model.addAttribute("currentCustomer", currentCustomer);
        return "user/home";
    }


}
