package com.example.cosmetics.controller;

import com.example.cosmetics.entity.Categories;
import com.example.cosmetics.entity.Product;
import com.example.cosmetics.service.CategoryService;
import com.example.cosmetics.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String home(Model model) {
        List<Categories> categories = categoryService.getAllCategories();
        List<Product> featuredProducts = productService.findFeaturedProducts();
        model.addAttribute("categories", categories);
        model.addAttribute("featuredProducts", featuredProducts);
        return "home";
    }

}