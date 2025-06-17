package com.example.demo.controller.admin.view;

import com.example.demo.dto.CustomerDTO;
import com.example.demo.dto.VehicleDTO;
import com.example.demo.entity.Customer;
import com.example.demo.entity.FuelType;
import com.example.demo.entity.Vehicle;
import com.example.demo.service.BrandService;
import com.example.demo.service.CustomerService;
import com.example.demo.service.ModelService;
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
@RequestMapping("/admin/vehicles")
public class VehicleViewController {

    private final VehicleService vehicleService;
    private final CustomerService customerService;
    private final BrandService brandService;
    private final ModelService modelService;

    @Autowired
    public VehicleViewController(VehicleService vehicleService, CustomerService customerService, BrandService brandService, ModelService modelService) {
        this.vehicleService = vehicleService;
        this.customerService = customerService;
        this.brandService = brandService;
        this.modelService = modelService;
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
        return "redirect:/admin/vehicles";
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
        model.addAttribute("brands", brandService.getAllBrands());


        return "admin/vehicle/createVehicle";
    }

    @PostMapping("/createVehicle")
    public String createVehicle(@ModelAttribute VehicleDTO vehicleDTO,
                                RedirectAttributes redirectAttributes){
        vehicleService.createVehicle(vehicleDTO);
        redirectAttributes.addFlashAttribute("successMesage", "Vehicle added");
        return "redirect:/admin/vehicles";
    }

    @GetMapping("/add/{customerId}")
    public String showCreateVehicleForCustomerByAdmin(@PathVariable Long customerId, Model model){
        Customer customer = customerService.getCustomerById(customerId);
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setCustomerId(customerId);

        List<Integer> years = IntStream.rangeClosed(1990, LocalDate.now().getYear())
                .boxed()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        model.addAttribute("years", years);
        model.addAttribute("vehicleDTO", vehicleDTO);
        model.addAttribute("fuelTypes", FuelType.values());
        model.addAttribute("brands", brandService.getAllBrands());

        return "admin/vehicle/createVehicleForCustomer";
    }
    @PostMapping("/add/{customerId}")
    public String createVehicleForCustomer(@PathVariable Long customerId,
                                           @ModelAttribute VehicleDTO vehicleDTO,
                                           RedirectAttributes redirectAttributes) {
        Customer customer = customerService.getCustomerById(customerId);
        vehicleDTO.setCustomerId(customerId);
        vehicleService.createVehicle(vehicleDTO);

        redirectAttributes.addFlashAttribute("successMessage", "Vehicle added successfully");
        return "redirect:/admin/customers/infoCustomer/" + customerId;
    }


    @GetMapping("/editVehicle/{id}")
    public String showEditVehicleForm(@PathVariable Long id, Model model){
        Vehicle vehicle = vehicleService.getVehicleById(id);
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setId(vehicle.getId());
        vehicleDTO.setBrandId(vehicle.getBrand().getId());
        vehicleDTO.setModelId(vehicle.getModel().getId());
        vehicleDTO.setRegistrationPlate(vehicle.getRegistrationPlate());
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
        return "redirect:/admin/vehicles/infoVehicle/" + id;
    }

    @GetMapping("/editVehicleForCustomer/{id}")
    public String showEditVehicleFormForCustomer(@PathVariable Long id, Model model){
        Vehicle vehicle = vehicleService.getVehicleById(id);

        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setId(vehicle.getId());
        vehicleDTO.setBrandId(vehicle.getBrand().getId());
        vehicleDTO.setModelId(vehicle.getModel().getId());
        vehicleDTO.setRegistrationPlate(vehicle.getRegistrationPlate());
        vehicleDTO.setYear(vehicle.getYear());
        vehicleDTO.setVin(vehicle.getVin());
        vehicleDTO.setEngineCapacity(vehicle.getEngineCapacity());
        vehicleDTO.setFuelType(vehicle.getFuelType());
        vehicleDTO.setDescription(vehicle.getDescription());
        model.addAttribute("vehicleDTO", vehicleDTO);
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("models", modelService.getModelsByBrandIdApi(vehicle.getBrand().getId()));

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

        return "admin/vehicle/editVehicleForCustomer";
    }

    @PostMapping("/editVehicleForCustomer/{id}")
    public String updateVehicleForCustomer(@PathVariable Long id,
                                           @ModelAttribute("vehicleDTO") @Valid VehicleDTO vehicleDTO,
                                           RedirectAttributes redirectAttributes){
        vehicleService.updateVehicle(vehicleDTO.getId(), vehicleDTO);
        Vehicle updatedVehicle = vehicleService.getVehicleById(id);
        Long customerId = updatedVehicle.getCustomer().getId();
        redirectAttributes.addFlashAttribute("successMessage", "Vehicle updated");
        return "redirect:/admin/customers/infoCustomer/" + customerId;
    }


}
