package com.example.demo.service;

import com.example.demo.dto.VehicleInspectionDTO;
import com.example.demo.entity.VehicleInspection;

import java.util.List;

public interface VehicleInspectionService {

    List<VehicleInspection> getAllInspections();

    List<VehicleInspection> getInspectionByVehicleId(Long vehicleId);

    VehicleInspection createInspection(Long vehicleId, VehicleInspectionDTO inspectionDTO);

    void deleteInspection(Long inspectionId);
}
