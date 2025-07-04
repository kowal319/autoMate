package com.example.demo.controller.customer;


import com.example.demo.dto.VehicleDTO;
import com.example.demo.dto.VehicleInspectionDTO;
import com.example.demo.entity.Customer;
import com.example.demo.entity.FuelType;
import com.example.demo.entity.Vehicle;
import com.example.demo.entity.VehicleInspection;
import com.example.demo.service.CustomerService;
import com.example.demo.service.VehicleInspectionService;
import com.example.demo.service.VehicleService;
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
@RequestMapping("/vehicles/{vehicleId}/inspection")
public class VehicleInspectionViewController {

    private final VehicleInspectionService vehicleInspectionService;
    private final CustomerService customerService;
    private final VehicleService vehicleService;


    public VehicleInspectionViewController(VehicleInspectionService vehicleInspectionService, CustomerService customerService, VehicleService vehicleService) {
        this.vehicleInspectionService = vehicleInspectionService;
        this.customerService = customerService;
        this.vehicleService = vehicleService;
    }

    @GetMapping("/history")
    public String showInspectionHistory(@PathVariable("vehicleId") Long vehicleId, Model model, Authentication authentication){
        String email = authentication.getName();
        Customer customer = customerService.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User Not found"));

        Vehicle vehicle = vehicleService.findByIdAndCustomer(vehicleId, customer)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied"));

        List<VehicleInspection> inspections = vehicleInspectionService.getInspectionByVehicleId(vehicleId);
        model.addAttribute("inspections", inspections);
        model.addAttribute("vehicle", vehicle);

        return "customer/vehicle/inspection/inspectionsList";
    }

        @GetMapping("/createInspection")
        public String showCreateInspectionForm(@PathVariable Long vehicleId,
                                               Model model,
                                               Authentication authentication) {
            String email = authentication.getName();
            Customer customer = customerService.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

            Vehicle vehicle = vehicleService.findByIdAndCustomer(vehicleId, customer)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied"));

            model.addAttribute("vehicleInspectionDTO", new VehicleInspectionDTO());
            model.addAttribute("vehicle", vehicle);

            return "customer/vehicle/inspection/createInspection";
        }

    @PostMapping("/createInspection")
    public String createInspection(@PathVariable Long vehicleId,
                                  @ModelAttribute @Valid VehicleInspectionDTO inspectionDTO,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {
        String email = authentication.getName();
        Optional<Customer> currentCustomer = customerService.findByEmail(email);

        if (currentCustomer.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "User not found.");
            return "redirect:/profileUser";
        }

        vehicleInspectionService.createInspection(vehicleId, inspectionDTO);

        redirectAttributes.addFlashAttribute("success", "Inspection added successfully.");
        return "redirect:/profileUser";
    }

    @PostMapping("/{inspectionId}/delete")
    public String deleteInspection(@PathVariable Long vehicleId,
                                   @PathVariable Long inspectionId,
                                   RedirectAttributes redirectAttributes,
                                   Authentication authentication) {
        String email = authentication.getName();
        Customer customer = customerService.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        Vehicle vehicle = vehicleService.findByIdAndCustomer(vehicleId, customer)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied"));

        vehicleInspectionService.deleteInspection(inspectionId);

        redirectAttributes.addFlashAttribute("success", "Inspection deleted successfully.");
        return "redirect:/vehicles/" + vehicleId + "/inspection/history";
    }
}
