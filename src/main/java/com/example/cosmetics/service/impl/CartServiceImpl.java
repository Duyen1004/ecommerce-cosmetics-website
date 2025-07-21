package com.example.cosmetics.service.impl;

import com.example.cosmetics.entity.Cart;
import com.example.cosmetics.entity.User;
import com.example.cosmetics.entity.Product;
import com.example.cosmetics.repository.CartRepository;
import com.example.cosmetics.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Override
    @Transactional
    public void addToCart(User user, Product product, int quantity) throws Exception {
        if (quantity <= 0) {
            throw new Exception("Số lượng phải lớn hơn 0");
        }
        if (product.getQuantity() < quantity) {
            throw new Exception("Sản phẩm không đủ số lượng trong kho");
        }

        Cart existingCart = cartRepository.findByUserAndProduct(user, product);
        if (existingCart != null) {
            // Nếu sản phẩm đã có trong giỏ hàng, cập nhật số lượng
            existingCart.setQuantity(existingCart.getQuantity() + quantity);
            cartRepository.save(existingCart);
        } else {
            // Nếu chưa có, tạo mục giỏ hàng mới
            Cart cart = new Cart();
            cart.setUser(user);
            cart.setProduct(product);
            cart.setQuantity(quantity);
            cart.setAddedAt(LocalDate.now());
            cartRepository.save(cart);
        }
    }

    @Override
    public List<Cart> getCartByUser(User user) {
        return cartRepository.findByUser(user);
    }

    @Override
    public void removeFromCart(Integer cartId) throws Exception {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new Exception("Không tìm thấy mục giỏ hàng"));
        cartRepository.delete(cart);
    }

    @Override
    @Transactional
    public void updateCartQuantity(Integer cartId, int quantity) throws Exception {
        if (quantity <= 0) {
            throw new Exception("Số lượng phải lớn hơn 0");
        }
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new Exception("Không tìm thấy mục giỏ hàng"));
        if (cart.getProduct().getQuantity() < quantity) {
            throw new Exception("Sản phẩm không đủ số lượng trong kho");
        }
        cart.setQuantity(quantity);
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void clearCart(User user) {
        List<Cart> cartItems = cartRepository.findByUser(user);
        cartRepository.deleteAll(cartItems);
    }
}