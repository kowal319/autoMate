package com.example.demo.repository;


import com.example.demo.entity.VehicleInspection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleInspectionRepository extends JpaRepository<VehicleInspection, Long> {
}
