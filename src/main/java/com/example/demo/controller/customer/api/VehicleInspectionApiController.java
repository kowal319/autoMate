package com.example.demo.controller.customer.api;


import com.example.demo.dto.VehicleInspectionDTO;
import com.example.demo.entity.Customer;
import com.example.demo.entity.VehicleInspection;
import com.example.demo.service.CustomerService;
import com.example.demo.service.VehicleInspectionService;
import com.example.demo.service.VehicleInsuranceService;
import com.example.demo.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/vehicle")
public class VehicleInspectionApiController {

    private final VehicleInspectionService vehicleInspectionService;
    private final CustomerService customerService;
    private final VehicleService vehicleService;

    public VehicleInspectionApiController( VehicleInspectionService vehicleInspectionService, CustomerService customerService, VehicleService vehicleService) {
        this.vehicleInspectionService = vehicleInspectionService;
        this.customerService = customerService;
        this.vehicleService = vehicleService;
    }

    @PostMapping("/{vehicleId}/inspection")
    public ResponseEntity<VehicleInspection> addInspectionToVehicle(
            @PathVariable Long vehicleId,
            @RequestBody @Valid VehicleInspectionDTO inspectionDTO,
            Authentication authentication
            ){

        String email = authentication.getName();
        Customer customer = customerService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        VehicleInspection createdInspection = vehicleInspectionService.createInspection(vehicleId, inspectionDTO);
        return new ResponseEntity<>(createdInspection, HttpStatus.CREATED);
    }

    @GetMapping("/{vehicleId}/inspections")
    public ResponseEntity<List<VehicleInspection>> getInspectionForVehicle(
            @PathVariable Long vehicleId,
            Authentication authentication
    ){
        String email = authentication.getName();
        Customer customer = customerService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        List<VehicleInspection> inspections = vehicleInspectionService.getInspectionByVehicleId(vehicleId);
        return ResponseEntity.ok(inspections);
    }

    @DeleteMapping("/{vehicleId}/inspection/{id}")
    public ResponseEntity<?> deleteInspection(@PathVariable Long id,
                                              Authentication authentication){
        vehicleInspectionService.deleteInspection(id);
        return ResponseEntity.ok("Inspection deleted");
    }
}
