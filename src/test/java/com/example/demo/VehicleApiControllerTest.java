package com.example.demo;

import com.example.demo.controller.api.VehicleApiController;
import com.example.demo.dto.VehicleDTO;
import com.example.demo.entity.Brand;
import com.example.demo.entity.FuelType;
import com.example.demo.entity.Model;
import com.example.demo.entity.Vehicle;
import com.example.demo.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VehicleApiControllerTest {

    @Mock
    private VehicleService vehicleService;

    @InjectMocks
    private VehicleApiController vehicleApiController;

    private Vehicle vehicle;
    private VehicleDTO vehicleDTO;
    private Brand brand;
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Brand brand = new Brand();
        brand.setId(1L);
        brand.setName("BMW");

        Model model = new Model();
        model.setId(1L);
        model.setName("Series 3");
        model.setBrand(brand);

        vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setBrand(brand);
        vehicle.setModel(model);
        vehicle.setYear(2003);
        vehicle.setVin("1HGCM82633A004352");
        vehicle.setFuelType(FuelType.PETROL);
        vehicle.setEngineCapacity(3.0);
        vehicle.setDescription("My new BMW e46 330CI Sport in blue colour. I bought that car in 2018.");

        vehicleDTO = new VehicleDTO();
        vehicleDTO.setId(1L);
        vehicleDTO.setBrandId(1L);
        vehicleDTO.setModelId(1L);
        vehicleDTO.setYear(2003);
        vehicleDTO.setVin("1HGCM82633A004352");
        vehicleDTO.setFuelType(FuelType.PETROL);
        vehicleDTO.setEngineCapacity(3.0);
        vehicleDTO.setDescription("My new BMW e46 330CI Sport in blue colour. I bought that car in 2018.");
    }

    @Test
    void getAllVehicles_shouldReturnList(){
        List<Vehicle> vehicleList = List.of(vehicle);
        when(vehicleService.getAllVehicles()).thenReturn(vehicleList);

        ResponseEntity<List<Vehicle>> response = vehicleApiController.getAllVehicles();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(vehicleList, response.getBody());
        verify(vehicleService, times(1));
    }

    @Test
    void getVehicleById_found(){
        when(vehicleService.getVehicleById(1L)).thenReturn(vehicle);

        ResponseEntity<Vehicle> response = vehicleApiController.getVehicleById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(vehicle, response.getBody());
        verify(vehicleService).getVehicleById(1L);
    }

    @Test
    void getVehicleById_notFound(){
        when(vehicleService.getVehicleById(2L)).thenReturn(null);

        ResponseEntity<Vehicle> response = vehicleApiController.getVehicleById(2L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(vehicleService).getVehicleById(2L);
    }

    @Test
    void createVehicle_shouldReturnCreatedVehicle(){
        when(vehicleService.createVehicle(vehicleDTO)).thenReturn(vehicle);
        ResponseEntity<Vehicle> response = vehicleApiController.createVehicle(vehicleDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(vehicle, response.getBody());
        verify(vehicleService).createVehicle(vehicleDTO);
    }

    @Test
    void updateVehicle_shouldReturnUpdatedVehicle(){
        when(vehicleService.updateVehicle(1l, vehicleDTO)).thenReturn(vehicle);

        ResponseEntity<Vehicle> response = vehicleApiController.updateVehicle(1L, vehicleDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(vehicle, response.getBody());
        verify(vehicleService).updateVehicle(1L, vehicleDTO);
    }

    @Test
    void deleteVehicle_returnNoContent(){
        Long id = 1L;

        ResponseEntity<Void> response = vehicleApiController.deleteVehicle(id);

        assertEquals(204, response.getStatusCodeValue());
        verify(vehicleService, times(1)).deleteVehicle(id);
    }
}
