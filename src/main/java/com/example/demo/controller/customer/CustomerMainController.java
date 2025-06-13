package com.example.demo.controller.customer;

import com.example.demo.dto.CustomerDTO;
import com.example.demo.entity.Customer;
import com.example.demo.service.CustomerService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

        if (currentCustomer.isPresent()) {
            Customer customer = currentCustomer.get();
            model.addAttribute("customer", customer);
            model.addAttribute("vehicles", customer.getVehicles());
        } else {
            return "redirect:/login";
        }

        return "customer/home";
    }

    @GetMapping("/edit")
    public String myProfileEditForm(Model model, Authentication authentication) {
        String email = authentication.getName();
        Customer customer = customerService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setEmail(customer.getEmail());
        dto.setName(customer.getName());

        model.addAttribute("customerDTO", dto);
        return "customer/myProfileEdit";
    }

    @PostMapping("/edit")
    public String myProfileEditSave(@ModelAttribute CustomerDTO updatedCustomer, Authentication authentication) {
        String email = authentication.getName();
        Customer customer = customerService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customerService.updateCustomer(customer.getId(), updatedCustomer);
        return "redirect:/profileUser";
    }

    @GetMapping("/changePassword")
    public String showChangePasswordForm(Model model, Authentication authentication) {
        String email = authentication.getName();
        Customer customer = customerService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        model.addAttribute("customerId", customer.getId()); // jeśli potrzebujesz ID np. w formularzu
        return "customer/changePassword";
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam String password,
                                 @RequestParam String confirmPassword,
                                 RedirectAttributes redirectAttributes,
                                 Authentication authentication) {
        String email = authentication.getName();
        Customer customer = customerService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Hasła nie są takie same");
            return "redirect:/profileUser/changePassword";
        }

        customerService.changePassword(customer.getId(), password);
        redirectAttributes.addFlashAttribute("successMessage", "Hasło zmienione");
        return "redirect:/profileUser";
    }



}
