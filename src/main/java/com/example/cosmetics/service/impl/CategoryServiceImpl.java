package com.example.cosmetics.service.impl;

import com.example.cosmetics.entity.Categories;
import com.example.cosmetics.repository.CategoryRepository;
import com.example.cosmetics.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public List<Categories> getAllCategories() {
        return categoryRepository.findAll();
    }

}
