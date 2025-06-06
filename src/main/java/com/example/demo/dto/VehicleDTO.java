package com.example.demo.dto;

import com.example.demo.entity.FuelType;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter

public class VehicleDTO {

    private Long id;
    private String brand;
    private String model;
    private String registrationPlate;
    private int year;
    private String vin;
    private double engineCapacity;
    private FuelType fuelType;
    private String description;
    private Long customerId;

}
