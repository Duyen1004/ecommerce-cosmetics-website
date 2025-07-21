package com.example.cosmetics.controller;

import com.example.cosmetics.entity.Categories;
import com.example.cosmetics.entity.Product;
import com.example.cosmetics.service.CategoryService;
import com.example.cosmetics.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ShopController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("/shop")
    public String shop(Model model,
                       @RequestParam(value = "categoryId", required = false) Integer categoryId,
                       @RequestParam(value = "search", required = false) String search) {
        List<Categories> categories = categoryService.getAllCategories();
        List<Product> products;

        if (search != null && !search.trim().isEmpty()) {
            products = productService.searchProducts(search);
        } else if (categoryId != null) {
            products = productService.findByCategoryId(categoryId);
        } else {
            products = productService.findAllProducts();
        }

        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        return "shop";
    }
}