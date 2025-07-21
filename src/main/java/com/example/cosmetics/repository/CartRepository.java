package com.example.cosmetics.repository;

import com.example.cosmetics.entity.Cart;
import com.example.cosmetics.entity.User;
import com.example.cosmetics.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findByUser(User user);
    Cart findByUserAndProduct(User user, Product product);
}