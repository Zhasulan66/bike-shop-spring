package com.example.bike_shop_spring.services;

import com.example.bike_shop_spring.models.Category;
import com.example.bike_shop_spring.models.Image;
import com.example.bike_shop_spring.models.Product;
import com.example.bike_shop_spring.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public Product getProduct(Long id){
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public void saveProduct(Product product, MultipartFile file1, MultipartFile file2,
                            MultipartFile file3, Long category_id) throws IOException {
        Image image1;
        Image image2;
        Image image3;
        if (file1.getSize() != 0) {
            image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            product.addImageToProduct(image1);
        }
        if (file2.getSize() != 0) {
            image2 = toImageEntity(file2);
            product.addImageToProduct(image2);
        }
        if (file3.getSize() != 0) {
            image3 = toImageEntity(file3);
            product.addImageToProduct(image3);
        }
        Category cat = new Category();
        cat = categoryService.findById(category_id);
        product.addCategoryToProduct(cat);
        log.info("Saving new Product. Title: {}; Author: {}", product.getTitle(), product.getAuthor());
        Product productFromDb = productRepository.save(product);
        productFromDb.setPreviewImageId(productFromDb.getImages().get(0).getId());
        productRepository.save(product);
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }

    public List<Product> getAllProductsByCat(Long cat_id){
        List<Product> cat_products = new ArrayList<>();
        for(Product prod : getAllProducts()){
            if(prod.getCategories().get(0).getId() == cat_id)
                cat_products.add(prod);
        }
        return cat_products;
    }

    public List<Product> getProductsByTitle(String title) {
        List<Product> title_products = new ArrayList<>();
        for(Product prod : getAllProducts()){
            if(prod.getTitle().equalsIgnoreCase(title))
                title_products.add(prod);
        }
        return title_products;
    }
}
