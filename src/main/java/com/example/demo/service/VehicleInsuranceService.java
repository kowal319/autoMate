package com.example.demo.service;

import com.example.demo.dto.VehicleDTO;
import com.example.demo.dto.VehicleInsuranceDTO;
import com.example.demo.entity.VehicleInsurance;

import java.util.List;

public interface VehicleInsuranceService {


    List<VehicleInsurance> getAllInsurances();

    List<VehicleInsurance> getInsurancesByVehicleId(Long vehicleId);

    VehicleInsurance createInsurance(Long vehicleId, VehicleInsuranceDTO insuranceDTO);

    void deleteInsurance(Long insuranceId);


}
