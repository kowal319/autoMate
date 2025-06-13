package com.example.demo.service;

import com.example.demo.dto.VehicleDTO;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Vehicle;

import java.util.List;

public interface VehicleService {

    Vehicle createVehicle(VehicleDTO vehicleDTO);
    Vehicle getVehicleById(Long id);
    List<Vehicle> getAllVehicles();
    Vehicle updateVehicle(Long id, VehicleDTO updatedVehicleDTO);
    String deleteVehicle(Long id);

    List<Vehicle> getVehiclesByCustomerId(Long customerId);

    void createVehicleForCustomer(VehicleDTO dto, Customer customer);
}
