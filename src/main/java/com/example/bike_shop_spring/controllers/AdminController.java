package com.example.bike_shop_spring.controllers;

import com.example.bike_shop_spring.models.Category;
import com.example.bike_shop_spring.models.Product;
import com.example.bike_shop_spring.models.Status;
import com.example.bike_shop_spring.models.User;
import com.example.bike_shop_spring.services.CategoryService;
import com.example.bike_shop_spring.services.ProductService;
import com.example.bike_shop_spring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(ProductService productService, CategoryService categoryService, UserService userService, PasswordEncoder passwordEncoder) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('developers:write')")
    public String showMain(){
        return "admin-cms";
    }

    @GetMapping("/products")
    public String findAllProducts(Model model){
        model.addAttribute("products", productService.getAllProducts());
        return "adm-products";
    }

    @GetMapping("/products/{id}")
    @PreAuthorize("hasAuthority('developers:write')")
    public String showProduct(@PathVariable Long id, Model model){
        Product product = productService.getProduct(id);
        model.addAttribute("product", product);
        model.addAttribute("images", product.getImages());
        return "adm-product-info";
    }

    @GetMapping("/users")
    public String findAllUser(Model model){
        model.addAttribute("users", userService.findAll());
        return "adm-users";
    }

    @GetMapping("/user/{id}")
    public String showUser(@PathVariable Long id, Model model){
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "adm-user-info";
    }


    @PostMapping("/user/delete/{id}")
    @PreAuthorize("hasAuthority('developers:write')")
    public String deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/user/{id}/edit")
    @PreAuthorize("hasAuthority('developers:write')")
    public String updateUserForm(@PathVariable("id") Long id, Model model){
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("status", Status.values());
        return "adm-user-update";
    }

    @PostMapping("/adm-user-update")
    @PreAuthorize("hasAuthority('developers:write')")
    public String updateUser(User user) {
        userService.saveUser(user);
        return "redirect:/admin/users";
    }



}
