package com.example.demo.service;

import com.example.demo.dto.BrandDTO;
import com.example.demo.entity.Brand;

import java.util.List;

public interface BrandService {
    List<Brand> getAllBrands();

    Brand getBrandById(Long id);

    Brand createNewBrand(BrandDTO brandDTO);

    String deleteBrand(Long id);

    Brand updateBrand(Long id, BrandDTO updatedBrand);
}
