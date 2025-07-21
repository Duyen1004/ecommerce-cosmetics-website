package com.example.cosmetics.controller;

import com.example.cosmetics.entity.Product;
import com.example.cosmetics.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ShopDetailController {
    @Autowired
    private ProductService productService;

    @GetMapping("/shop-detail")
    public String shopDetail(Model model, @RequestParam("productId") Integer productId) {
        Product product = productService.findById(productId);
        if (product == null) {
            // Nếu sản phẩm không tồn tại hoặc ẩn (status = 0), chuyển hướng về shop
            return "redirect:/shop";
        }
        model.addAttribute("product", product);
        return "shop-detail";
    }
}