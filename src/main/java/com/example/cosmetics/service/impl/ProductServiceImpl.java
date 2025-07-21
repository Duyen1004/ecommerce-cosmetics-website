package com.example.cosmetics.service.impl;

import com.example.cosmetics.entity.Product;
import com.example.cosmetics.repository.ProductRepository;
import com.example.cosmetics.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> findFeaturedProducts() {
        return productRepository.findFeaturedProducts();
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findByCategoryId(Integer categoryId) {
        return productRepository.findByCategoryCategoryId(categoryId);
    }

    @Override
    public Product findById(Integer productId) {
        return productRepository.findByProductId(productId);
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByProductNameContainingIgnoreCase(keyword);
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }
}