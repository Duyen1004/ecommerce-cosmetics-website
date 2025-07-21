package com.example.cosmetics.controller;

import com.example.cosmetics.entity.Cart;
import com.example.cosmetics.entity.Product;
import com.example.cosmetics.entity.User;
import com.example.cosmetics.service.CartService;
import com.example.cosmetics.service.ProductService;
import com.example.cosmetics.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("productId") Integer productId,
                            @RequestParam(value = "quantity", defaultValue = "1") Integer quantity,
                            Authentication authentication,
                            Model model) {
        try {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            if (user == null) {
                model.addAttribute("error", "Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng.");
                return "redirect:/login";
            }
            Product product = productService.findById(productId);
            if (product == null) {
                model.addAttribute("error", "Sản phẩm không tồn tại.");
                return "redirect:/shop";
            }
            cartService.addToCart(user, product, quantity);
            model.addAttribute("message", "Đã thêm sản phẩm vào giỏ hàng!");
            return "redirect:/shop-detail?productId=" + productId;
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/shop-detail?productId=" + productId;
        }
    }

    @GetMapping("/cart")
    public String viewCart(Authentication authentication, Model model) {
        try {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            if (user == null) {
                model.addAttribute("error", "Vui lòng đăng nhập để xem giỏ hàng.");
                return "redirect:/login";
            }
            List<Cart> cartItems = cartService.getCartByUser(user);
            double totalPrice = cartItems.stream()
                    .filter(cart -> cart.getProduct() != null && cart.getProduct().getPrice() != null)
                    .mapToDouble(cart -> cart.getProduct().getPrice()
                            .multiply(BigDecimal.valueOf(cart.getQuantity()))
                            .doubleValue())
                    .sum();
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("totalPrice", totalPrice);
            return "shopping-cart";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            return "shopping-cart";
        }
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam("cartId") Integer cartId, Model model) {
        try {
            cartService.removeFromCart(cartId);
            model.addAttribute("message", "Đã xóa sản phẩm khỏi giỏ hàng!");
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/update")
    public String updateCartQuantity(@RequestParam("cartId") Integer cartId,
                                     @RequestParam("action") String action,
                                     Authentication authentication,
                                     Model model) {
        try {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            if (user == null) {
                model.addAttribute("error", "Vui lòng đăng nhập để cập nhật giỏ hàng.");
                return "redirect:/login";
            }
            Cart cart = cartService.getCartByUser(user).stream()
                    .filter(c -> c.getCartId() == cartId)
                    .findFirst()
                    .orElseThrow(() -> new Exception("Không tìm thấy mục giỏ hàng"));

            int newQuantity = cart.getQuantity();
            if ("increase".equals(action)) {
                newQuantity++;
            } else if ("decrease".equals(action)) {
                newQuantity--;
            }

            if (newQuantity <= 0) {
                cartService.removeFromCart(cartId);
                model.addAttribute("message", "Đã xóa sản phẩm khỏi giỏ hàng!");
            } else {
                cartService.updateCartQuantity(cartId, newQuantity);
                model.addAttribute("message", "Đã cập nhật số lượng sản phẩm!");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/cart";
    }

    
}