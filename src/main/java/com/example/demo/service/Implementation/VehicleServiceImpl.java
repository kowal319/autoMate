package com.example.demo.service.Implementation;


import com.example.demo.dto.VehicleDTO;
import com.example.demo.entity.Brand;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Model;
import com.example.demo.entity.Vehicle;
import com.example.demo.repository.BrandRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ModelRepository;
import com.example.demo.repository.VehicleRepository;
import com.example.demo.service.VehicleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;
    private final BrandRepository brandRepository;
    private final ModelRepository modelRepository;

    public VehicleServiceImpl(VehicleRepository vehicleRepository, CustomerRepository customerRepository, BrandRepository brandRepository, ModelRepository modelRepository) {
        this.vehicleRepository = vehicleRepository;
        this.customerRepository = customerRepository;
        this.brandRepository = brandRepository;
        this.modelRepository = modelRepository;
    }

    @Override
    public Vehicle createVehicle(VehicleDTO vehicleDTO) {

        System.out.println("vehicleDTO.brandId = " + vehicleDTO.getBrandId());
        System.out.println("vehicleDTO.modelId = " + vehicleDTO.getModelId());

        Brand brand = brandRepository.findById(vehicleDTO.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        Model model = modelRepository.findById(vehicleDTO.getModelId())
                .orElseThrow(() -> new RuntimeException("Model not found"));

        Customer customer = customerRepository.findById(vehicleDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Vehicle vehicle = new Vehicle();
        vehicle.setBrand(brand);
        vehicle.setModel(model);
        vehicle.setCustomer(customer);

        vehicle.setRegistrationPlate(vehicleDTO.getRegistrationPlate());
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
        // Pobieranie brand
        Brand brand = brandRepository.findById(updatedVehicleDTO.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found"));
        existing.setBrand(brand);

        // Pobieranie model
        Model model = modelRepository.findById(updatedVehicleDTO.getModelId())
                .orElseThrow(() -> new RuntimeException("Model not found"));
        existing.setModel(model);
        existing.setRegistrationPlate(updatedVehicleDTO.getRegistrationPlate());
        existing.setVin(updatedVehicleDTO.getVin());
        existing.setYear(updatedVehicleDTO.getYear());
        existing.setFuelType(updatedVehicleDTO.getFuelType());
        existing.setEngineCapacity(updatedVehicleDTO.getEngineCapacity());
        existing.setDescription(updatedVehicleDTO.getDescription());

//        Customer customer = customerRepository.findById(updatedVehicleDTO.getCustomerId())
//                .orElseThrow(() -> new RuntimeException("Customer not found"));
//        existing.setCustomer(customer);

        return vehicleRepository.save(existing);
    }

    @Override
    public Vehicle updateBasicInfoInCustomerEditVehicle(Long id, VehicleDTO updatedVehicleDTO){
        Vehicle existing = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
        existing.setRegistrationPlate(updatedVehicleDTO.getRegistrationPlate());
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

    @Override
    public List<Vehicle> getVehiclesByCustomerId(Long customerId) {
        return vehicleRepository.findByCustomerId(customerId);
    }


    //Customer side

    @Override
    public void createVehicleForCustomer(VehicleDTO dto, Customer customer) {

        Brand brand = brandRepository.findById(dto.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        Model model = modelRepository.findById(dto.getModelId())
                .orElseThrow(() -> new RuntimeException("Model not found"));


        Vehicle vehicle = new Vehicle();
        vehicle.setBrand(brand);
        vehicle.setModel(model);
        vehicle.setCustomer(customer);

        vehicle.setRegistrationPlate(dto.getRegistrationPlate());
        vehicle.setVin(dto.getVin());
        vehicle.setEngineCapacity(dto.getEngineCapacity());
        vehicle.setFuelType(dto.getFuelType());
        vehicle.setYear(dto.getYear());
        vehicle.setDescription(dto.getDescription());

        customer.getVehicles().add(vehicle);

        vehicleRepository.save(vehicle);
    }

    @Override
    public Optional<Vehicle> findByIdAndCustomer(Long vehicleId, Customer customer) {
        return vehicleRepository.findByIdAndCustomerId(vehicleId, customer.getId());
    }

    @Override
    public void changeVehicleOwner(Long vehicleId, Long newCustomerId){
            Vehicle vehicle = vehicleRepository.findById(vehicleId)
                    .orElseThrow(() -> new RuntimeException("Vehicle not found"));

            Customer customer = customerRepository.findById(newCustomerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            vehicle.setCustomer(customer);
            vehicleRepository.save(vehicle);
        }
    }

