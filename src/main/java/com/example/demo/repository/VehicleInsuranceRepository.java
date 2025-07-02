package com.example.demo.repository;

import com.example.demo.entity.VehicleInsurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface VehicleInsuranceRepository extends JpaRepository<VehicleInsurance, Long> {
    List<VehicleInsurance> findByVehicleId(Long vehicleId);
}
