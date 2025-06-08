package com.example.demo.controller.api;

import com.example.demo.entity.Model;
import com.example.demo.service.ModelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands/{brandId}/models")
public class ModelApiController {

    private final ModelService modelService;

    public ModelApiController(ModelService modelService) {
        this.modelService = modelService;
    }

    @GetMapping
    public List<Model> getModelsByBrandId(@PathVariable Long brandId) {
        return modelService.getModelsByBrandId(brandId);
    }
}
