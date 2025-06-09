package com.example.demo.service;

import com.example.demo.dto.ModelDTO;
import com.example.demo.entity.Model;

import java.util.List;

public interface ModelService {
    List<Model> getModelsByBrandId(Long brandId);

    List<ModelDTO> getModelsByBrandIdApi(Long brandId);

    Model createNewModel(Long id, ModelDTO modelDTO);

    Model updateModel(Long id, ModelDTO modelDTO);

    void deleteModel(Long id);

    Model getModelById(Long id);
}
