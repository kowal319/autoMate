package com.example.demo.controller.view;

import com.example.demo.dto.CustomerDTO;
import com.example.demo.entity.Customer;
import com.example.demo.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomerViewController {

    private final CustomerService customerService;

    @Autowired
    public CustomerViewController(CustomerService customerService){
        this.customerService = customerService;
    }

    @GetMapping()
    public String listAllCustomers(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "customers";
    }

    @GetMapping("/{id}")
    public String getCustomerById(@PathVariable Long id, Model model) {
        Customer customer = customerService.getCustomerById(id);
        model.addAttribute("customer", customer);
        return "customerId";
    }

    @PostMapping("delete/{id}")
    public String deleteCustomer(@PathVariable Long id){
        customerService.deleteCustomer(id);
        return "redirect:/customers";
    }

    @PostMapping("/addCustomer")
    public String createCustomer(@ModelAttribute CustomerDTO customerDTO, RedirectAttributes redirectAttributes){
        customerService.createCustomer(customerDTO);
        redirectAttributes.addFlashAttribute("successMessage", "uzytkownik dodany");
        return "redirect:/customers";
    }

}

