package com.example.demo.controller.view;

import com.example.demo.dto.CustomerDTO;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Vehicle;
import com.example.demo.service.CustomerService;
import com.example.demo.service.VehicleService;
import jakarta.validation.Valid;
import org.apache.catalina.util.CustomObjectInputStream;
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
    private final VehicleService vehicleService;


    @Autowired
    public CustomerViewController(CustomerService customerService, VehicleService vehicleService){
        this.customerService = customerService;
        this.vehicleService = vehicleService;
    }

    @GetMapping()
    public String listAllCustomers(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "admin/customers";
    }

    @GetMapping("/infoCustomer/{id}")
    public String getCustomerById(@PathVariable Long id, Model model) {
        Customer customer = customerService.getCustomerById(id);
        model.addAttribute("customer", customer);

        List<Vehicle> vehicles = vehicleService.getVehiclesByCustomerId(id);
        model.addAttribute("vehicles", vehicles);
        return "admin/infoCustomer";
    }

    @PostMapping("delete/{id}")
    public String deleteCustomer(@PathVariable Long id){
        customerService.deleteCustomer(id);
        return "redirect:/customers";
    }

    @GetMapping("/createCustomer")
    public String showCreateForm(Model model) {
        model.addAttribute("customerDTO", new CustomerDTO());
        return "admin/createCustomer";
    }

    @PostMapping("/createCustomer")
    public String createCustomer(@ModelAttribute CustomerDTO customerDTO, RedirectAttributes redirectAttributes){
        customerService.createCustomer(customerDTO);
        redirectAttributes.addFlashAttribute("successMessage", "uzytkownik dodany");
        return "redirect:/customers";
    }

    @GetMapping("/editCustomer/{id}")
    public String showEditForm(@PathVariable Long id, Model model){
        Customer customer = customerService.getCustomerById(id);
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        model.addAttribute("customerDTO", dto);
        return "admin/editCustomer";
    }

    @PostMapping("/editCustomer/{id}")
    public String updateCustomer(@PathVariable Long id,
                                 @ModelAttribute("customerDT0") @Valid CustomerDTO customerDTO,
                                 RedirectAttributes redirectAttributes){
        customerService.updateCustomer(id, customerDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Dane zostaly zmienione");
        return "redirect:/customers";
    }

    @GetMapping("/changePassword/{id}")
    public String showChangePasswordForm(@PathVariable Long id, Model model){
        model.addAttribute("customerId", id);
        return "admin/changePassword";
    }

    @PostMapping("/changePassword/{id}")
    public String changePassword(@PathVariable Long id,
                                 @RequestParam String password,
                                 @RequestParam String confirmPassword,
                                 RedirectAttributes redirectAttributes){
        if (!password.equals(confirmPassword)){
            redirectAttributes.addFlashAttribute("errorMessage", "Hasla nie sa takie same");
            return "redirect:/customers/changePassword" + id;
        }
        customerService.changePassword(id, password);
        redirectAttributes.addFlashAttribute("successMessage", " Haslo zmienione");
        return "redirect:/customers";
    }

}