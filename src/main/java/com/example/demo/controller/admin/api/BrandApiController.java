package com.example.demo.controller.admin.api;


import com.example.demo.dto.BrandDTO;
import com.example.demo.entity.Brand;
import com.example.demo.service.BrandService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
public class BrandApiController {


    private final BrandService brandService;

    @Autowired
    public BrandApiController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public ResponseEntity<List<Brand>> getAllBrands(){
        System.out.println("🔥 [API] Przed Wywołaniem getAllBrands()");
        List<Brand> brands = brandService.getAllBrands();
        System.out.println("🔥 [API] Wywołano getAllBrands()");

        return ResponseEntity.ok(brands);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Brand> getBrandById(@PathVariable Long id){
        Brand brand = brandService.getBrandById(id);
        if(brand != null){
            return ResponseEntity.ok(brand);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping()
        public ResponseEntity<Brand> createBrand(@RequestParam @Valid BrandDTO brandDTO){
        Brand savedBrand = brandService.createNewBrand(brandDTO);
        return ResponseEntity.ok(savedBrand);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Brand> updateBrand(@PathVariable Long id, @RequestBody @Valid BrandDTO brandDTO){
        Brand updateBrand = brandService.updateBrand(id, brandDTO);
        return ResponseEntity.ok(updateBrand);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id){
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }
}
