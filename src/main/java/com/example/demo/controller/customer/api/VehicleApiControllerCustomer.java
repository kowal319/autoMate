package com.example.demo.controller.customer.api;


import com.example.demo.dto.VehicleDTO;
import com.example.demo.entity.Customer;
import com.example.demo.entity.FuelType;
import com.example.demo.entity.Vehicle;
import com.example.demo.service.BrandService;
import com.example.demo.service.CustomerService;
import com.example.demo.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/customer/vehicles")
public class VehicleApiControllerCustomer {

    private final CustomerService customerService;
    private final VehicleService vehicleService;
    private final BrandService brandService;

    public VehicleApiControllerCustomer(CustomerService customerService, VehicleService vehicleService, BrandService brandService) {
        this.customerService = customerService;
        this.vehicleService = vehicleService;
        this.brandService = brandService;
    }

    @PostMapping
    public ResponseEntity<?> createVehicle(@RequestBody @Valid VehicleDTO vehicleDTO,
                                           Authentication authentication) {
        String email = authentication.getName();
        Optional<Customer> customerOpt = customerService.findByEmail(email);

        if (customerOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Customer not found.");
        }

        vehicleService.createVehicleForCustomer(vehicleDTO, customerOpt.get());
        return ResponseEntity.ok("Vehicle added successfully.");
    }

    @GetMapping("/vehicle/{id}")
    public ResponseEntity<Vehicle> getVehicle(@PathVariable Long id, Authentication auth) {
        String email = auth.getName();
        Customer customer = customerService.findByEmail(email).orElseThrow();
        Optional<Vehicle> vehicle = vehicleService.findByIdAndCustomer(id, customer);
        return vehicle.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable Long id,
                                           Authentication authentication) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.ok("Vehicle deleted.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVehicle(@PathVariable Long id,
                                           @RequestBody @Valid VehicleDTO vehicleDTO,
                                           Authentication authentication) {
        vehicleDTO.setId(id);
        vehicleService.updateBasicInfoInCustomerEditVehicle(id, vehicleDTO);
        System.out.println(vehicleDTO.getEngineCapacity());
        return ResponseEntity.ok("Vehicle updated.");
    }

    @GetMapping("/form-data")
    public ResponseEntity<?> getFormData() {
        List<Integer> years = IntStream.rangeClosed(1990, LocalDate.now().getYear())
                .boxed()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        Map<String, Object> formData = new HashMap<>();
        formData.put("fuelTypes", FuelType.values());
        formData.put("brands", brandService.getAllBrands());
        formData.put("years", years);

        return ResponseEntity.ok(formData);
    }

    @GetMapping
    public ResponseEntity<?> getAllVehiclesForCurrentCustomer(Authentication authentication) {
        String email = authentication.getName();
        Customer customer = customerService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        List<Vehicle> vehicles = vehicleService.getVehiclesByCustomerId(customer.getId());

        List<VehicleDTO> dtos = vehicles.stream().map(vehicle -> {
            VehicleDTO dto = new VehicleDTO();
            dto.setId(vehicle.getId());
            dto.setRegistrationPlate(vehicle.getRegistrationPlate());
            dto.setFuelType(vehicle.getFuelType());
            dto.setYear(vehicle.getYear());
            dto.setVin(vehicle.getVin());
            dto.setDescription(vehicle.getDescription());
            dto.setBrandId(vehicle.getBrand().getId());
            dto.setBrandName(vehicle.getBrand().getName());
            dto.setModelId(vehicle.getModel().getId());
            dto.setModelName(vehicle.getModel().getName());
            dto.setEngineCapacity(vehicle.getEngineCapacity());
            dto.setCustomerId(vehicle.getCustomer().getId());
            return dto;
        }).toList();

        return ResponseEntity.ok(dtos);
    }


}
