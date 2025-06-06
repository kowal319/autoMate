package com.example.demo.controller.view;

import com.example.demo.dto.VehicleDTO;
import com.example.demo.entity.FuelType;
import com.example.demo.entity.Vehicle;
import com.example.demo.service.CustomerService;
import com.example.demo.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/vehicles")
public class VehicleViewController {

    private final VehicleService vehicleService;
    private final CustomerService customerService;

    @Autowired
    public VehicleViewController(VehicleService vehicleService, CustomerService customerService) {
        this.vehicleService = vehicleService;
        this.customerService = customerService;
    }

    @GetMapping
    public String listAllVehicles(Model model){
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        model.addAttribute("vehicles", vehicles);
        return "admin/vehicle/vehicles";
    }

    @GetMapping("/infoVehicle/{id}")
    public String getVehicleById(@PathVariable Long id, Model model){
        Vehicle vehicle = vehicleService.getVehicleById(id);
        model.addAttribute("vehicle", vehicle);
        return "admin/vehicle/infoVehicle";
    }

    @PostMapping("delete/{id}")
    public String deleteVehicle(@PathVariable Long id){
        vehicleService.deleteVehicle(id);
        return "redirect:/vehicles";
    }

    @GetMapping("/createVehicle")
    public String showCreateVehicleForm(Model model) {
        model.addAttribute("vehicleDTO", new VehicleDTO());
        model.addAttribute("fuelTypes", FuelType.values());

        List<Integer> years = IntStream.rangeClosed(1990, LocalDate.now().getYear())
                .boxed()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        model.addAttribute("years", years);
        model.addAttribute("customers", customerService.getAllCustomers());

        return "admin/vehicle/createVehicle";
    }

    @PostMapping("/createVehicle")
    public String createVehicle(@ModelAttribute VehicleDTO vehicleDTO, RedirectAttributes redirectAttributes){
        vehicleService.createVehicle(vehicleDTO);
        redirectAttributes.addFlashAttribute("successMesage", "Vehicle added");
        return "redirect:/vehicles";
    }

    @GetMapping("/editVehicle/{id}")
    public String showEditVehicleForm(@PathVariable Long id, Model model){
        Vehicle vehicle = vehicleService.getVehicleById(id);
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setId(vehicle.getId());
        vehicleDTO.setBrand(vehicle.getBrand());
        vehicleDTO.setModel(vehicle.getModel());
        vehicleDTO.setYear(vehicle.getYear());
        vehicleDTO.setVin(vehicle.getVin());
        vehicleDTO.setEngineCapacity(vehicle.getEngineCapacity());
        vehicleDTO.setFuelType(vehicle.getFuelType());
        vehicleDTO.setDescription(vehicle.getDescription());
        model.addAttribute("vehicleDTO", vehicleDTO);

        model.addAttribute("fuelTypes", FuelType.values());

        List<Integer> years = IntStream.rangeClosed(1990, LocalDate.now().getYear())
                .boxed()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        model.addAttribute("years", years);

        if (vehicle.getCustomer() != null) {
            vehicleDTO.setCustomerId(vehicle.getCustomer().getId());
        }

        model.addAttribute("customers", customerService.getAllCustomers());

        return "admin/vehicle/editVehicle";
    }

    @PostMapping("/editVehicle/{id}")
    public String updateVehicle(@PathVariable Long id,
                                @ModelAttribute("vehicleDTO") @Valid VehicleDTO vehicleDTO,
                                RedirectAttributes redirectAttributes){
        vehicleService.updateVehicle(id, vehicleDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Vehicle updated");
        return "redirect:/vehicles/infoVehicle/" + id;    }


}
