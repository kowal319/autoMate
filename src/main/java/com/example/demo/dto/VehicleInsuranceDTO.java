package com.example.demo.dto;


import com.example.demo.entity.Vehicle;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class VehicleInsuranceDTO {

    private int id;
    private String insuranceNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private String insuranceCompany;
    private String additionalInfo;
    private Vehicle vehicle;

}
