package com.example.demo.service.Implementation;


import com.example.demo.dto.VehicleDTO;
import com.example.demo.entity.Vehicle;
import com.example.demo.repository.VehicleRepository;
import com.example.demo.service.VehicleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }


    @Override
    public Vehicle createVehicle(VehicleDTO vehicleDTO) {
       Vehicle vehicle = new Vehicle();
       vehicle.setBrand(vehicleDTO.getBrand());
       vehicle.setModel(vehicleDTO.getModel());
       vehicle.setVin(vehicleDTO.getVin());
       vehicle.setEngineCapacity(vehicleDTO.getEngineCapacity());
       vehicle.setFuelType(vehicleDTO.getFuelType());
       vehicle.setYear(vehicleDTO.getYear());
       vehicle.setDescription(vehicleDTO.getDescription());
       return vehicleRepository.save(vehicle);
    }

    @Override
    public Vehicle getVehicleById(Long id) {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(id);
        return vehicleOptional.orElse(null);
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @Override
    public Vehicle updateVehicle(Long id, VehicleDTO updatedVehicleDTO) {
       Vehicle existing = vehicleRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Not found"));
        existing.setBrand(updatedVehicleDTO.getBrand());
        existing.setModel(updatedVehicleDTO.getModel());
        existing.setVin(updatedVehicleDTO.getVin());
        existing.setYear(updatedVehicleDTO.getYear());
        existing.setFuelType(updatedVehicleDTO.getFuelType());
        existing.setEngineCapacity(updatedVehicleDTO.getEngineCapacity());
        existing.setDescription(updatedVehicleDTO.getDescription());
        return vehicleRepository.save(existing);
    }

    @Override
    public String deleteVehicle(Long id) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(id);
        if(optionalVehicle.isPresent()){
            Vehicle vehicle = optionalVehicle.get();
            vehicleRepository.deleteById(id);
            return "Vehicle with id " + id + " has been deleted";
        } else{
            return "Vehicle with id " + id + " has not found";
        }
    }
}
