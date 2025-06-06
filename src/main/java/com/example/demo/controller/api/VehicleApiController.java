package com.example.demo.controller.api;


import com.example.demo.dto.VehicleDTO;
import com.example.demo.entity.Vehicle;
import com.example.demo.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleApiController {

    private final VehicleService vehicleService;

    @Autowired
    public VehicleApiController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles(){
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id){
        Vehicle vehicle = vehicleService.getVehicleById(id);
        if(vehicle != null){
            return ResponseEntity.ok(vehicle);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@RequestBody @Valid VehicleDTO vehicleDTO){
        Vehicle savedVehicle = vehicleService.createVehicle(vehicleDTO);
        return ResponseEntity.ok(savedVehicle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id, @RequestBody @Valid VehicleDTO vehicleDTO){
        Vehicle updatedVehicle = vehicleService.updateVehicle(id, vehicleDTO);
        return ResponseEntity.ok(updatedVehicle);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id){
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }


}
