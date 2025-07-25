package com.example.demo.controller.customer;

import com.example.demo.dto.VehicleDTO;
import com.example.demo.entity.*;
import com.example.demo.service.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/profileUser")
public class VehicleViewControllerCustomer{

    private final CustomerService customerService;
    private final VehicleService vehicleService;
    private final BrandService brandService;
    private final VehicleInsuranceService vehicleInsuranceService;
    private final VehicleInspectionService vehicleInspectionService;

    public VehicleViewControllerCustomer(CustomerService customerService, VehicleService vehicleService, BrandService brandService, VehicleInsuranceService vehicleInsuranceService, VehicleInspectionService vehicleInspectionService) {
        this.customerService = customerService;
        this.vehicleService = vehicleService;
        this.brandService = brandService;
        this.vehicleInsuranceService = vehicleInsuranceService;
        this.vehicleInspectionService = vehicleInspectionService;
    }

    @GetMapping("/createMyVehicle")
    public String showCreateVehicleForm(Model model) {
        model.addAttribute("vehicleDTO", new VehicleDTO());
        model.addAttribute("fuelTypes", FuelType.values());

        List<Integer> years = IntStream.rangeClosed(1990, LocalDate.now().getYear())
                .boxed()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        model.addAttribute("years", years);
        model.addAttribute("brands", brandService.getAllBrands());


        return "customer/vehicle/createMyVehicle";
    }

    @PostMapping("/createMyVehicle")
    public String createMyVehicle(@ModelAttribute VehicleDTO vehicleDTO,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {
        String email = authentication.getName();
        Optional<Customer> currentCustomer = customerService.findByEmail(email);

        if (currentCustomer.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "User not found.");
            return "redirect:/profileUser";
        }

        vehicleService.createVehicleForCustomer(vehicleDTO, currentCustomer.get());

        redirectAttributes.addFlashAttribute("success", "Vehicle added successfully.");
        return "redirect:/profileUser";
    }

    @GetMapping("/infoVehicle/{id}")
    public String showVehicleDetails(@PathVariable Long id, Model model, Authentication auth) {
        String email = auth.getName();
        Customer customer = customerService.findByEmail(email).orElseThrow();

        Vehicle vehicle = vehicleService.findByIdAndCustomer(id, customer)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied"));

        model.addAttribute("vehicle", vehicle);

        List<VehicleInsurance> insurances = vehicleInsuranceService.getInsurancesByVehicleId(id);
        model.addAttribute("insurances", insurances);

        List<VehicleInspection> inspections = vehicleInspectionService.getInspectionByVehicleId(id);
        model.addAttribute("inspections", inspections);


        return "customer/vehicle/vehicleInfo";
    }

    @PostMapping("deleteVehicle/{id}")
    public String deleteVehicle(@PathVariable Long id){
        vehicleService.deleteVehicle(id);
        return "redirect:/profileUser";
    }

    @GetMapping("/editVehicle/{id}")
    public String showEditVehicleForm(@PathVariable Long id, Model model, Authentication auth) {
        String email = auth.getName();
        Customer customer = customerService.findByEmail(email).orElseThrow();

        Vehicle vehicle = vehicleService.findByIdAndCustomer(id, customer)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied"));
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setId(vehicle.getId());
        vehicleDTO.setRegistrationPlate(vehicle.getRegistrationPlate());
        vehicleDTO.setDescription(vehicle.getDescription());
        model.addAttribute("vehicleDTO", vehicleDTO);

        if (vehicle.getCustomer() != null) {
            vehicleDTO.setCustomerId(vehicle.getCustomer().getId());
        }
        return "customer/vehicle/vehicleEdit";
    }
    @PostMapping("/editVehicle/{id}")
    public String updateVehicle(@PathVariable Long id,
                                @ModelAttribute("vehicleDTO") @Valid VehicleDTO vehicleDTO,
                                RedirectAttributes redirectAttributes){
        vehicleDTO.setId(id);
        vehicleService.updateBasicInfoInCustomerEditVehicle(id, vehicleDTO);
        redirectAttributes.addFlashAttribute("successMessage", "vehicle updated");
        return "redirect:/profileUser/infoVehicle/" + id;
    }
}

