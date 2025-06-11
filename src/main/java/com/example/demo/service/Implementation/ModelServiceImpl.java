package com.example.demo.service.Implementation;


import com.example.demo.dto.ModelDTO;
import com.example.demo.entity.Brand;
import com.example.demo.entity.Model;
import com.example.demo.repository.BrandRepository;
import com.example.demo.repository.ModelRepository;
import com.example.demo.repository.VehicleRepository;
import com.example.demo.service.ModelService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModelServiceImpl implements ModelService {

    private final ModelRepository modelRepository;
    private final BrandRepository brandRepository;
    private final VehicleRepository vehicleRepository;

    public ModelServiceImpl(ModelRepository modelRepository, BrandRepository brandRepository, VehicleRepository vehicleRepository) {
        this.modelRepository = modelRepository;
        this.brandRepository = brandRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public List<Model> getModelsByBrandId(Long brandId){
        return modelRepository.findByBrandId(brandId);
    }


    @Override
    public List<ModelDTO> getModelsByBrandIdApi(Long brandId) {
        List<Model> models = modelRepository.findByBrandId(brandId);
        return models.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Model createNewModel(Long id, ModelDTO modelDTO){
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        Model model = new Model();
        model.setName(modelDTO.getName());
        model.setBrand(brand);

        return modelRepository.save(model);
    }

    @Override
    public ModelDTO getModelForBrand(Long brandId, Long modelId) {
        Model model = modelRepository.findById(modelId)
                .orElseThrow(() -> new EntityNotFoundException("Model not found"));

        if (model.getBrand() == null || !model.getBrand().getId().equals(brandId)) {
            throw new EntityNotFoundException("Model does not belong to the specified brand");
        }

        return mapToDTO(model);
    }

    @Override
    public Model save(Model model) {
        return modelRepository.save(model);
    }

    @Override
    public Model updateModel(Long id, ModelDTO modelDTO) {
        Model existing = modelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Model not found"));
        existing.setName(modelDTO.getName());
        return modelRepository.save(existing);
    }

    @Override
    public void deleteModel(Long id) {
        modelRepository.deleteById(id);
    }

    @Override
    public Model getModelById(Long id) {
        return modelRepository.findById(id).orElse(null);
    }

    private ModelDTO mapToDTO(Model model) {
        ModelDTO dto = new ModelDTO();
        dto.setId(model.getId());
        dto.setName(model.getName());
        if (model.getBrand() != null) {
            dto.setBrandId(model.getBrand().getId());
        }
        return dto;
    }
    }


