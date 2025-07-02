package com.example.demo.controller.customer;


import com.example.demo.entity.Customer;
import com.example.demo.entity.Vehicle;
import com.example.demo.entity.VehicleInsurance;
import com.example.demo.service.CustomerService;
import com.example.demo.service.VehicleInsuranceService;
import com.example.demo.service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/vehicles/{vehicleId}/insurance")
public class VehicleInsuranceViewController {

    private final VehicleInsuranceService vehicleInsuranceService;
    private final CustomerService customerService;
    private final VehicleService vehicleService;

    public VehicleInsuranceViewController(VehicleInsuranceService vehicleInsuranceService, CustomerService customerService, VehicleService vehicleService) {
        this.vehicleInsuranceService = vehicleInsuranceService;
        this.customerService = customerService;
        this.vehicleService = vehicleService;
    }

    @GetMapping("/history")
    public String showInsuranceHistory(@PathVariable("vehicleId") Long vehicleId, Model model, Authentication authentication) {
        String email = authentication.getName();
        Customer customer = customerService.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        Vehicle vehicle = vehicleService.findByIdAndCustomer(vehicleId, customer)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied"));

        List<VehicleInsurance> insurances = vehicleInsuranceService.getInsurancesByVehicleId(vehicleId);
        model.addAttribute("insurances", insurances);
        model.addAttribute("vehicle", vehicle);

        return "customer/vehicle/insurance/insurancesList";
    }

}
