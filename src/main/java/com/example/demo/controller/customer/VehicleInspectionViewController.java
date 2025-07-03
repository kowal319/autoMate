package com.example.demo.controller.customer;


import com.example.demo.entity.Customer;
import com.example.demo.entity.Vehicle;
import com.example.demo.entity.VehicleInspection;
import com.example.demo.service.CustomerService;
import com.example.demo.service.VehicleInspectionService;
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

@Controller
@RequestMapping("/vehicles/{vehicleId}/inspection")
public class VehicleInspectionViewController {

    private final VehicleInspectionService vehicleInspectionService;
    private final CustomerService customerService;
    private final VehicleService vehicleService;


    public VehicleInspectionViewController(VehicleInspectionService vehicleInspectionService, CustomerService customerService, VehicleService vehicleService) {
        this.vehicleInspectionService = vehicleInspectionService;
        this.customerService = customerService;
        this.vehicleService = vehicleService;
    }

    @GetMapping("/history")
    public String showInspectionHistory(@PathVariable("vehicleId") Long vehicleId, Model model, Authentication authentication){
        String email = authentication.getName();
        Customer customer = customerService.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User Not found"));

        Vehicle vehicle = vehicleService.findByIdAndCustomer(vehicleId, customer)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied"));

        List<VehicleInspection> inspections = vehicleInspectionService.getInspectionByVehicleId(vehicleId);
        model.addAttribute("inspections", inspections);
        model.addAttribute("vehicle", vehicle);

        return "customer/vehicle/inspection/inspectionList";
    }
}
