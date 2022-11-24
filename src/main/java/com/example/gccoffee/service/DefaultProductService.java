package com.example.gccoffee.service;

import com.example.gccoffee.model.Category;
import com.example.gccoffee.model.Product;
import com.example.gccoffee.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DefaultProductService implements ProductService {

    private final ProductRepository repository;

    public DefaultProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Product> getProductsByCategory(Category category) {
        return repository.findByCategory(category);
    }

    @Override
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    @Override
    public Product createProduct(String productName, Category category, long price) {
        var product = new Product(UUID.randomUUID(),  productName, category, price);
        return repository.insert(product);
    }

    @Override
    public Product createProduct(String productName, Category category, long price, String description) {
        var product = new Product(UUID.randomUUID(),  productName, category, price, description, LocalDateTime.now(), LocalDateTime.now());
        return repository.insert(product);

    }
}
