package com.example.gccoffee.controller.api;


import com.example.gccoffee.model.Category;
import com.example.gccoffee.model.Product;
import com.example.gccoffee.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductRestController {

    private final ProductService service;

    public ProductRestController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/api/v1/products")
    public List<Product> productList(@RequestParam Optional<Category> category){
        return category
                .map(service::getProductsByCategory)
                .orElse(service.getAllProducts());
    }
}
