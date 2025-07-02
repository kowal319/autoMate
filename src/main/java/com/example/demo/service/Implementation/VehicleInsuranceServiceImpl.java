package com.example.demo.service.Implementation;


import com.example.demo.dto.VehicleInsuranceDTO;
import com.example.demo.entity.Vehicle;
import com.example.demo.entity.VehicleInsurance;
import com.example.demo.repository.VehicleInsuranceRepository;
import com.example.demo.repository.VehicleRepository;
import com.example.demo.service.VehicleInsuranceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleInsuranceServiceImpl implements VehicleInsuranceService {

private final VehicleRepository vehicleRepository;
private final VehicleInsuranceRepository vehicleInsuranceRepository;

    public VehicleInsuranceServiceImpl(VehicleRepository vehicleRepository, VehicleInsuranceRepository vehicleInsuranceRepository) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleInsuranceRepository = vehicleInsuranceRepository;
    }

    @Override
    public List<VehicleInsurance> getAllInsurances() {
        return vehicleInsuranceRepository.findAll();
    }

    @Override
    public List<VehicleInsurance> getInsurancesByVehicleId(Long vehicleId) {
        return vehicleInsuranceRepository.findByVehicleId(vehicleId).reversed();
    }

    @Override
    public VehicleInsurance createInsurance(Long vehicleId, VehicleInsuranceDTO insuranceDTO) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        VehicleInsurance insurance = new VehicleInsurance();
        insurance.setInsuranceNumber(insuranceDTO.getInsuranceNumber());
        insurance.setStartDate(insuranceDTO.getStartDate());
        insurance.setEndDate(insuranceDTO.getEndDate());
        insurance.setInsuranceCompany(insuranceDTO.getInsuranceCompany());
        insurance.setAdditionalInfo(insuranceDTO.getAdditionalInfo());

        insurance.setVehicle(vehicle);

        return vehicleInsuranceRepository.save(insurance);
    }

    @Override
    public void deleteInsurance(Long insuranceId) {
        if (!vehicleInsuranceRepository.existsById(insuranceId)) {
            throw new RuntimeException("Insurance not found");
        }
        vehicleInsuranceRepository.deleteById(insuranceId);

    }
}
