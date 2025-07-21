package com.example.cosmetics.service;

import com.example.cosmetics.entity.Cart;
import com.example.cosmetics.entity.User;
import com.example.cosmetics.entity.Product;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CartService {
    void addToCart(User user, Product product, int quantity) throws Exception;
    List<Cart> getCartByUser(User user);
    void removeFromCart(Integer cartId) throws Exception;
    void updateCartQuantity(Integer cartId, int quantity) throws Exception;
    void clearCart(User user) throws Exception;
}