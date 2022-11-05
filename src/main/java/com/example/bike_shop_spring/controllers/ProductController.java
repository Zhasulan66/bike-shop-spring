package com.example.bike_shop_spring.controllers;

import com.example.bike_shop_spring.models.Product;
import com.example.bike_shop_spring.services.CategoryService;
import com.example.bike_shop_spring.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String showMain(){
        return "home-page";
    }

    @GetMapping("/products")
    public String findAll(Model model){
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("categories", categoryService.findAll());
        return "products";
    }

    @GetMapping("/products/{id}")
    @PreAuthorize("hasAuthority('developers:read')")
    public String showProduct(@PathVariable Long id, Model model){
        Product product = productService.getProduct(id);
        model.addAttribute("product", product);
        model.addAttribute("images", product.getImages());
        return "product-info";
    }

    @GetMapping("/products/create")
    @PreAuthorize("hasAuthority('developers:write')")
    public String createProductForm(Product product, Model model){
        model.addAttribute("categories", categoryService.findAll());
        return "product-create";
    }

    @PostMapping("/products/create")
    @PreAuthorize("hasAuthority('developers:write')")
    public String createProduct(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2,
                                @RequestParam("file3") MultipartFile file3, @RequestParam("category_id") Long category_id,
                                Product product) throws IOException {
        productService.saveProduct(product, file1, file2, file3, category_id);
        return "redirect:/products";
    }

    @PostMapping("/products/delete/{id}")
    @PreAuthorize("hasAuthority('developers:write')")
    public String deleteProduct(@PathVariable("id") Long id){
        productService.deleteProduct(id);
        return "redirect:/products";
    }

    @GetMapping("/products/{id}/edit")
    @PreAuthorize("hasAuthority('developers:write')")
    public String updateProductForm(@PathVariable("id") Long id, Model model){
        Product product = productService.getProduct(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.findAll());
        return "product-update";
    }

    @PostMapping("/product-update")
    @PreAuthorize("hasAuthority('developers:write')")
    public String updateProduct(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2,
                                @RequestParam("file3") MultipartFile file3, @RequestParam("category_id") Long category_id,
                                Product product) throws IOException{
        productService.saveProduct(product, file1, file2, file3, category_id);
        return "redirect:/products";
    }


    @GetMapping("/products/find-cat")
    public String findByCat(@RequestParam("category_id") Long category_id, Model model){

        model.addAttribute("products", productService.getAllProductsByCat(category_id));
        model.addAttribute("categories", categoryService.findAll());
        return "products-cat";
    }

    @GetMapping("/products/find-title")
    public String findByTitle(@RequestParam("product_title") String title, Model model){

        model.addAttribute("products", productService.getProductsByTitle(title));
        model.addAttribute("categories", categoryService.findAll());
        return "products-cat";
    }
}
