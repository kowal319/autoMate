package com.example.demo;

import com.example.demo.controller.admin.api.ModelApiController;
import com.example.demo.dto.ModelDTO;
import com.example.demo.entity.Brand;
import com.example.demo.entity.Model;
import com.example.demo.service.BrandService;
import com.example.demo.service.ModelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ModelApiControllerTest {

    @Mock
    private ModelService modelService;

    @Mock
    private BrandService brandService;

    @InjectMocks
    private ModelApiController modelApiController;

    private Brand brand;
    private Model model;
    private ModelDTO modelDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        brand = new Brand();
        brand.setId(1L);
        brand.setName("BMW");

        model = new Model();
        model.setId(5L);
        model.setName("X5");
        model.setBrand(brand);

        modelDTO = new ModelDTO();
        modelDTO.setId(5L);
        modelDTO.setName("X5");
        modelDTO.setBrandId(1L);
    }

    @Test
    void getModelsByBrandId_shouldReturnList() {
        List<ModelDTO> modelList = List.of(modelDTO);
        when(modelService.getModelsByBrandIdApi(1L)).thenReturn(modelList);

        List<ModelDTO> result = modelApiController.getModelsByBrandId(1L);

        assertEquals(1, result.size());
        assertEquals("X5", result.get(0).getName());
        assertEquals(1L, result.get(0).getBrandId());
        verify(modelService).getModelsByBrandIdApi(1L);
    }

    @Test
    void getModelForBrand_shouldReturnModelDTO() {
        when(modelService.getModelForBrand(1L, 5L)).thenReturn(modelDTO);

        ResponseEntity<ModelDTO> response = modelApiController.getModelForBrand(1L, 5L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(modelDTO, response.getBody());
        assertEquals(1L, response.getBody().getBrandId());
        verify(modelService).getModelForBrand(1L, 5L);
    }

    @Test
    void createModel_shouldReturnSavedModel() {
        when(brandService.getBrandById(1L)).thenReturn(brand);
        when(modelService.save(any(Model.class))).thenReturn(model);

        ResponseEntity<Model> response = modelApiController.createModel(1L, modelDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("X5", response.getBody().getName());
        assertEquals(1L, response.getBody().getBrand().getId());
        verify(brandService).getBrandById(1L);
        verify(modelService).save(any(Model.class));
    }

    @Test
    void createModel_shouldReturnNotFound_whenBrandIsNull() {
        when(brandService.getBrandById(99L)).thenReturn(null);

        ResponseEntity<Model> response = modelApiController.createModel(99L, modelDTO);

        assertEquals(404, response.getStatusCodeValue());
        verify(brandService).getBrandById(99L);
        verify(modelService, never()).save(any());
    }

    @Test
    void updateModel_shouldReturnUpdatedModel() {
        when(modelService.updateModel(5L, modelDTO)).thenReturn(model);

        ResponseEntity<Model> response = modelApiController.updateModel(5L, modelDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("X5", response.getBody().getName());
        assertEquals(1L, response.getBody().getBrand().getId());
        verify(modelService).updateModel(5L, modelDTO);
    }

    @Test
    void deleteModel_shouldReturnNoContent() {
        ResponseEntity<Void> response = modelApiController.deleteModel(5L);

        assertEquals(204, response.getStatusCodeValue());
        verify(modelService).deleteModel(5L);
    }
}
