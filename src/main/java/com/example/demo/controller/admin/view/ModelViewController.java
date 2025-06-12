package com.example.demo.controller.admin.view;


import com.example.demo.dto.ModelDTO;
import com.example.demo.entity.Brand;
import com.example.demo.entity.Model;
import com.example.demo.service.BrandService;
import com.example.demo.service.ModelService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("brands/{brandId}/models")
public class ModelViewController {


    private final ModelService modelService;
    private final BrandService brandService;

    public ModelViewController(ModelService modelService, BrandService brandService) {
        this.modelService = modelService;
        this.brandService = brandService;
    }

    @GetMapping
    public String listModels(@PathVariable Long brandId, ModelMap model) {
        List<Model> models = modelService.getModelsByBrandId(brandId);
        Brand brand = brandService.getBrandById(brandId);

        model.addAttribute("models", models);
        model.addAttribute("brand", brand);

        return "admin/model/models";
    }

    @GetMapping("/create")
    public String showCreateModelForm(@PathVariable Long brandId, ModelMap model) {
        model.addAttribute("modelDTO", new ModelDTO());
        model.addAttribute("brandId", brandId);
        return "admin/model/createModel";
    }

    @PostMapping("/create")
    public String createModel(@PathVariable Long brandId,
                              @ModelAttribute("modelDTO") @Valid ModelDTO modelDTO,
                              RedirectAttributes redirectAttributes) {
        modelService.createNewModel(brandId, modelDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Model added successfully!");
        return "redirect:/brands/" + brandId + "/models";
    }

    @GetMapping("/edit/{id}")
    public String showEditModelForm(@PathVariable Long brandId,
                                    @PathVariable Long id,
                                    ModelMap model) {
        Model modelEntity = modelService.getModelById(id);
        ModelDTO modelDTO = new ModelDTO();
        modelDTO.setId(modelEntity.getId());
        modelDTO.setName(modelEntity.getName());

        model.addAttribute("modelDTO", modelDTO);
        model.addAttribute("brandId", brandId);
        return "admin/model/editModel";
    }

    @PostMapping("/edit/{id}")
    public String updateModel(@PathVariable Long brandId,
                              @PathVariable Long id,
                              @ModelAttribute("modelDTO") @Valid ModelDTO modelDTO,
                              RedirectAttributes redirectAttributes) {
        modelService.updateModel(id, modelDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Model updated!");
        return "redirect:/brands/" + brandId + "/models";
    }

    @PostMapping("/delete/{id}")
    public String deleteModel(@PathVariable Long brandId,
                              @PathVariable Long id,
                              RedirectAttributes redirectAttributes) {
        modelService.deleteModel(id);
        redirectAttributes.addFlashAttribute("successMessage", "Model deleted!");
        return "redirect:/brands/" + brandId + "/models";
    }

    @GetMapping("/info/{id}")
    public String showModelInfo(@PathVariable Long brandId,
                                @PathVariable Long id,
                                ModelMap model) {
        Model modelEntity = modelService.getModelById(id);
        model.addAttribute("model", modelEntity);
        model.addAttribute("brandId", brandId);
        return "admin/model/infoModel";
    }
}


