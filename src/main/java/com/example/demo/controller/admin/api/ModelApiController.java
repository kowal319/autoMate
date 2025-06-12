package com.example.demo.controller.admin.api;

import com.example.demo.dto.ModelDTO;
import com.example.demo.entity.Brand;
import com.example.demo.entity.Model;
import com.example.demo.service.BrandService;
import com.example.demo.service.ModelService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands/{brandId}/models")
public class ModelApiController {

    private final ModelService modelService;
    private final BrandService brandService;

    public ModelApiController(ModelService modelService, BrandService brandService) {
        this.modelService = modelService;
        this.brandService = brandService;
    }

    @GetMapping
    public List<ModelDTO> getModelsByBrandId(@PathVariable Long brandId) {
        return modelService.getModelsByBrandIdApi(brandId);
    }

    @GetMapping("/{modelId}")
    public ResponseEntity<ModelDTO> getModelForBrand(
            @PathVariable Long brandId,
            @PathVariable Long modelId) {

        ModelDTO modelDTO = modelService.getModelForBrand(brandId, modelId);
        return ResponseEntity.ok(modelDTO);
    }

    @PostMapping
    public ResponseEntity<Model> createModel(@PathVariable("brandId") Long brandId,
                                             @RequestBody @Valid ModelDTO modelDTO){
        Brand brand = brandService.getBrandById(brandId);
        if(brand == null){
            return ResponseEntity.notFound().build();
        }
        Model model = new Model();
        model.setName(modelDTO.getName());
        model.setBrand(brand);

        Model savedModel = modelService.save(model);
        return ResponseEntity.ok(savedModel);
   }

   @PutMapping("/{id}")
    public ResponseEntity<Model> updateModel(@PathVariable Long id, @RequestBody @Valid ModelDTO modelDTO){
        Model updateModel = modelService.updateModel(id, modelDTO);
        return ResponseEntity.ok(updateModel);
   }

   @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModel(@PathVariable Long id){
        modelService.deleteModel(id);
        return ResponseEntity.noContent().build();
   }
}
