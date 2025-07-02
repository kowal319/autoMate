package com.example.demo.controller.customer.api;

import com.example.demo.dto.VehicleDTO;
import com.example.demo.dto.VehicleInsuranceDTO;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Vehicle;
import com.example.demo.entity.VehicleInsurance;
import com.example.demo.service.CustomerService;
import com.example.demo.service.VehicleInsuranceService;
import com.example.demo.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customer/vehicle")
public class VehicleInsuranceApiController {

    private final VehicleInsuranceService vehicleInsuranceService;
    private final CustomerService customerService;
    private final VehicleService vehicleService;

    public VehicleInsuranceApiController(VehicleInsuranceService vehicleInsuranceService, CustomerService customerService, VehicleService vehicleService) {
        this.vehicleInsuranceService = vehicleInsuranceService;
        this.customerService = customerService;
        this.vehicleService = vehicleService;
    }

    @PostMapping("/{vehicleId}/insurance")
    public ResponseEntity<VehicleInsurance> addInsuranceToVehicle(
            @PathVariable Long vehicleId,
            @RequestBody @Valid VehicleInsuranceDTO insuranceDTO,
            Authentication authentication) {

        String email = authentication.getName();
        Customer customer = customerService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        VehicleInsurance createdInsurance = vehicleInsuranceService.createInsurance(vehicleId, insuranceDTO);
        return new ResponseEntity<>(createdInsurance, HttpStatus.CREATED);
    }

    @GetMapping("/{vehicleId}/insurances")
    public ResponseEntity<List<VehicleInsurance>> getInsurancesForVehicle(
            @PathVariable Long vehicleId,
            Authentication authentication) {

        String email = authentication.getName();
        Customer customer = customerService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        List<VehicleInsurance> insurances = vehicleInsuranceService.getInsurancesByVehicleId(vehicleId);
        return ResponseEntity.ok(insurances);
    }

    @DeleteMapping("/{vehicleId}/insurance/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable Long id,
                                           Authentication authentication) {
        vehicleInsuranceService.deleteInsurance(id);
        return ResponseEntity.ok("Insurance Deleted.");
    }
}


