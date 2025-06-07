package com.example.demo;

import com.example.demo.controller.api.BrandApiController;
import com.example.demo.dto.BrandDTO;
import com.example.demo.entity.Brand;
import com.example.demo.service.BrandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BrandApiControllerTest {

    @Mock
    private BrandService brandService;

    @InjectMocks
    private BrandApiController brandApiController;

    private Brand brand;
    private BrandDTO brandDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        brand = new Brand();
        brand.setId(1L);
        brand.setName("BMW");

        brandDTO = new BrandDTO();
        brandDTO.setName("BMW");
    }

    @Test
    void getAllBrands_shouldReturnList() {
        List<Brand> brands = List.of(brand);
        when(brandService.getAllBrands()).thenReturn(brands);

        ResponseEntity<List<Brand>> response = brandApiController.getAllBrands();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(brands, response.getBody());
        verify(brandService, times(1)).getAllBrands();
    }

    @Test
    void getBrandById_found() {
        when(brandService.getBrandById(1L)).thenReturn(brand);

        ResponseEntity<Brand> response = brandApiController.getBrandById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(brand, response.getBody());
        verify(brandService).getBrandById(1L);
    }

    @Test
    void getBrandById_notFound() {
        when(brandService.getBrandById(2L)).thenReturn(null);

        ResponseEntity<Brand> response = brandApiController.getBrandById(2L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(brandService).getBrandById(2L);
    }

    @Test
    void createBrand_shouldReturnCreatedBrand() {
        when(brandService.createNewBrand(brandDTO)).thenReturn(brand);

        ResponseEntity<Brand> response = brandApiController.createBrand(brandDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(brand, response.getBody());
        verify(brandService).createNewBrand(brandDTO);
    }

    @Test
    void updateBrand_shouldReturnUpdatedBrand() {
        when(brandService.updateBrand(1L, brandDTO)).thenReturn(brand);

        ResponseEntity<Brand> response = brandApiController.updateBrand(1L, brandDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(brand, response.getBody());
        verify(brandService).updateBrand(1L, brandDTO);
    }

    @Test
    void deleteBrand_shouldReturnNoContent() {
        when(brandService.deleteBrand(1L)).thenReturn("Brand deleted");

        ResponseEntity<Void> response = brandApiController.deleteBrand(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(brandService).deleteBrand(1L);
    }
}
