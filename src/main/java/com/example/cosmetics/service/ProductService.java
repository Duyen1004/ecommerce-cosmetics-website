package com.example.cosmetics.service;

import com.example.cosmetics.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findFeaturedProducts();
    List<Product> findAllProducts();
    List<Product> findByCategoryId(Integer categoryId);
    Product findById(Integer productId);
    List<Product> searchProducts(String keyword);
    void save(Product product);
}