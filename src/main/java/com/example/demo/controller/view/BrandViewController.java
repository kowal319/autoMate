package com.example.demo.controller.view;

import com.example.demo.dto.BrandDTO;
import com.example.demo.entity.Brand;
import com.example.demo.service.BrandService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/brands")
public class BrandViewController {

    private final BrandService brandService;

    @Autowired
    public BrandViewController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public String listAllBrands(Model model) {
        List<Brand> brands = brandService.getAllBrands();
        model.addAttribute("brands", brands);
        return "admin/brand/brands";
    }

    @GetMapping("/infoBrand/{id}")
    public String getBrandById(@PathVariable Long id, Model model) {
        Brand brand = brandService.getBrandById(id);
        model.addAttribute("brand", brand);
        return "admin/brand/infoBrand";
    }

    @PostMapping("/delete/{id}")
    public String deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return "redirect:/brands";
    }

    @GetMapping("/createBrand")
    public String showCreateBrandForm(Model model) {
        model.addAttribute("brandDTO", new BrandDTO());
        return "admin/brand/createBrand";
    }

    @PostMapping("/createBrand")
    public String createBrand(@ModelAttribute BrandDTO brandDTO,
                              RedirectAttributes redirectAttributes) {
        brandService.createNewBrand(brandDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Brand added successfully!");
        return "redirect:/brands";
    }

    @GetMapping("/editBrand/{id}")
    public String showEditBrandForm(@PathVariable Long id, Model model) {
        Brand brand = brandService.getBrandById(id);
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId(brand.getId());
        brandDTO.setName(brand.getName());
        model.addAttribute("brandDTO", brandDTO);
        return "admin/brand/editBrand";
    }

    @PostMapping("/editBrand/{id}")
    public String updateBrand(@PathVariable Long id,
                              @ModelAttribute("brandDTO") @Valid BrandDTO brandDTO,
                              RedirectAttributes redirectAttributes) {
        brandService.updateBrand(id, brandDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Brand updated successfully!");
        return "redirect:/brands";
    }
}
