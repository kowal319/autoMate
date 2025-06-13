package com.example.demo.controller.customer;

import com.example.demo.dto.VehicleDTO;
import com.example.demo.entity.Customer;
import com.example.demo.entity.FuelType;
import com.example.demo.service.BrandService;
import com.example.demo.service.CustomerService;
import com.example.demo.service.VehicleService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/profileUser")
public class VehicleViewControllerCustomerSide {

    private final CustomerService customerService;
    private final VehicleService vehicleService;
    private final BrandService brandService;

    public VehicleViewControllerCustomerSide(CustomerService customerService, VehicleService vehicleService, BrandService brandService) {
        this.customerService = customerService;
        this.vehicleService = vehicleService;
        this.brandService = brandService;
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
            return "redirect:/profileUser"; // lub inna bezpieczna strona
        }

        vehicleService.createVehicleForCustomer(vehicleDTO, currentCustomer.get());

        redirectAttributes.addFlashAttribute("success", "Vehicle added successfully.");
        return "redirect:/profileUser";
    }
}
