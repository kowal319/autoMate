package com.example.demo.controller.customer.api;


import com.example.demo.dto.CustomerDTO;
import com.example.demo.dto.VehicleDTO;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Vehicle;
import com.example.demo.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customer")
public class CustomerMainApiController {

    private final CustomerService customerService;

    public CustomerMainApiController(CustomerService customerService) {
        this.customerService = customerService;
    }

//    @GetMapping("/vehicles")
//    public ResponseEntity<List<VehicleDTO>> getVehicles(Authentication authentication) {
//        String email = authentication.getName();
//        Optional<Customer> customerOpt = customerService.findByEmail(email);
//
//        if (customerOpt.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        Customer customer = customerOpt.get();
//
//        List<VehicleDTO> dtoList = customer.getVehicles().stream().map(vehicle -> {
//            VehicleDTO dto = new VehicleDTO();
//            dto.setId(vehicle.getId());
//            dto.setRegistrationPlate(vehicle.getRegistrationPlate());
//            dto.setFuelType(vehicle.getFuelType());
//            dto.setYear(vehicle.getYear());
//            dto.setBrandId(vehicle.getBrand().getId());
//            dto.setModelId(vehicle.getModel().getId());
//            return dto;
//        }).toList();
//
//        return ResponseEntity.ok(dtoList);
//    }

    @GetMapping("/profile")
    public ResponseEntity<CustomerDTO> getProfile(Authentication authentication) {
        String email = authentication.getName();
        Optional<Customer> customerOpt = customerService.findByEmail(email);

        if (customerOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Customer customer = customerOpt.get();
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setEmail(customer.getEmail());
        dto.setName(customer.getName());

        return ResponseEntity.ok(dto);
    }


    // 🔹 Edytuj dane profilu
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody CustomerDTO updatedCustomer, Authentication authentication) {
        String email = authentication.getName();
        Customer customer = customerService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customerService.updateCustomer(customer.getId(), updatedCustomer);
        return ResponseEntity.ok().build();
    }

    // 🔹 Zmień hasło
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Authentication authentication) {

        if (!password.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body("Hasła nie są takie same");
        }

        String email = authentication.getName();
        Customer customer = customerService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customerService.changePassword(customer.getId(), password);
        return ResponseEntity.ok("Hasło zmienione");
    }
}
