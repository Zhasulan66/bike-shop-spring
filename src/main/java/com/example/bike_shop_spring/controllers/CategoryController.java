package com.example.bike_shop_spring.controllers;

import com.example.bike_shop_spring.models.Category;
import com.example.bike_shop_spring.models.Product;
import com.example.bike_shop_spring.repositories.CategoryRepository;
import com.example.bike_shop_spring.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public String findAll(Model model){
        model.addAttribute("categories", categoryService.findAll());
        return "categories";
    }

    @GetMapping("/category/{id}")
    public String showCategory(@PathVariable Long id, Model model){
        Category category = categoryService.findById(id);
        model.addAttribute("category", category);
        return "category-info";
    }

    @GetMapping("/category/create")
    public String createCategoryForm(Category category){
        return "category-create";
    }

    @PostMapping("/category/create")
    public String createCategory(Category category) {
        categoryService.saveCategory(category);
        return "redirect:/categories";
    }

    @PostMapping("/category/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id){
        categoryService.deleteCategory(id);
        return "redirect:/categories";
    }

    @GetMapping("/category/{id}/edit")
    @PreAuthorize("hasAuthority('developers:write')")
    public String updateCategoryForm(@PathVariable("id") Long id, Model model){
        Category category = categoryService.findById(id);
        model.addAttribute("category", category);
        return "category-update";
    }

    @PostMapping("/category-update")
    @PreAuthorize("hasAuthority('developers:write')")
    public String updateCategory(Category category) {
        categoryService.saveCategory(category);
        return "redirect:/categories";
    }
}
