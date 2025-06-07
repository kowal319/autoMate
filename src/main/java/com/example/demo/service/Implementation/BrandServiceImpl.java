package com.example.demo.service.Implementation;

import com.example.demo.dto.BrandDTO;
import com.example.demo.entity.Brand;
import com.example.demo.repository.BrandRepository;
import com.example.demo.service.BrandService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public List<Brand> getAllBrands(){
        return brandRepository.findAll();
    }

    @Override
    public Brand getBrandById(Long id){
        Optional<Brand> brandOptional = brandRepository.findById(id);
        return brandOptional.orElse(null);
    }

    @Override
    public Brand createNewBrand(BrandDTO brandDTO){
        Brand brand = new Brand();
        brand.setName(brandDTO.getName());
        return brandRepository.save(brand);
    }

    @Override
    public String deleteBrand(Long id) {
        Optional<Brand> brandOptional = brandRepository.findById(id);
        if (brandOptional.isPresent()) {
            Brand brand = brandOptional.get();
            brandRepository.deleteById(id);
            return "Brand deleted";
        } else {
            return "Brand not found";
        }
    }

    @Override
    public Brand updateBrand(Long id, BrandDTO updatedBrand){
        Brand existing = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found"));
        existing.setName(updatedBrand.getName());
        return brandRepository.save(existing);
    }

}
