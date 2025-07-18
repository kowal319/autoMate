package com.example.demo.service.Implementation;


import com.example.demo.dto.VehicleInspectionDTO;
import com.example.demo.entity.Vehicle;
import com.example.demo.entity.VehicleInspection;
import com.example.demo.repository.VehicleInspectionRepository;
import com.example.demo.repository.VehicleRepository;
import com.example.demo.service.VehicleInspectionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleInspectionServiceImpl implements VehicleInspectionService {

   private final VehicleRepository vehicleRepository;
   private final VehicleInspectionRepository vehicleInspectionRepository;

    public VehicleInspectionServiceImpl(VehicleRepository vehicleRepository, VehicleInspectionRepository vehicleInspectionRepository) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleInspectionRepository = vehicleInspectionRepository;
    }


    @Override
    public List<VehicleInspection> getAllInspections() {
        return vehicleInspectionRepository.findAll();
    }

    @Override
    public List<VehicleInspection> getInspectionByVehicleId(Long vehicleId) {
        return vehicleInspectionRepository.findByVehicleId(vehicleId);
    }

    @Override
    public VehicleInspection createInspection(Long vehicleId, VehicleInspectionDTO inspectionDTO) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        VehicleInspection inspection = new VehicleInspection();
        inspection.setStartDate(inspectionDTO.getStartDate());
        inspection.setEndDate(inspectionDTO.getEndDate());
        inspection.setInspectionCompany(inspectionDTO.getInspectionCompany());
        inspection.setAdditionalInfo(inspectionDTO.getAdditionalInfo());

        inspection.setVehicle(vehicle);
        return vehicleInspectionRepository.save(inspection);
    }

    @Override
    public void deleteInspection(Long inspectionId) {
        if(!vehicleInspectionRepository.existsById(inspectionId)){
            throw new RuntimeException("Inspection not found");
        } vehicleInspectionRepository.deleteById(inspectionId);

    }
}
