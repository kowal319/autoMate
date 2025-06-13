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

    @GetMapping("/edit/{id}")
    public String myProfileEditForm(@PathVariable Long id, Model model){
        Customer customer = customerService.getCustomerById(id);

        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setEmail(customer.getEmail());
        dto.setName(customer.getName());

        model.addAttribute("customerDTO", dto);
        return "customer/myProfileEdit";
    }

    @PostMapping("/edit/{id}")
    public String myProfileEditSave(@PathVariable Long id, @ModelAttribute CustomerDTO updatedCustomer){
        customerService.updateCustomer(id, updatedCustomer);
        return "redirect:/profileUser";
    }

    @GetMapping("/changePassword/{id}")
    public String showChangePasswordForm(@PathVariable Long id, Model model){
        model.addAttribute("customerId", id);
        return "customer/changePassword";
    }

    @PostMapping("/changePassword/{id}")
    public String changePassword(@PathVariable Long id,
                                 @RequestParam String password,
                                 @RequestParam String confirmPassword,
                                 RedirectAttributes redirectAttributes){
        if (!password.equals(confirmPassword)){
            redirectAttributes.addFlashAttribute("errorMessage", "Hasla nie sa takie same");
            return "redirect:/profileUser/changePassword/" + id;
        }
        customerService.changePassword(id, password);
        redirectAttributes.addFlashAttribute("successMessage", " Haslo zmienione");
        return "redirect:/profileUser";
    }



}
