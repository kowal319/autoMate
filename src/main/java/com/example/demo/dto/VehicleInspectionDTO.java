package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Setter
@Getter
public class VehicleInspectionDTO {
    private Long id;

    private LocalDate startDate;
    private LocalDate endDate;
    private String inspectionCompany;
    private String additionalInfo;


}
