package com.example.demo.controller.customer;


import com.example.demo.service.VehicleInsuranceService;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/vehicleInsurance")
public class VehicleInsuranceViewController {

    private final VehicleInsuranceService vehicleInsuranceService;

    public VehicleInsuranceViewController(VehicleInsuranceService vehicleInsuranceService) {
        this.vehicleInsuranceService = vehicleInsuranceService;
    }






}
