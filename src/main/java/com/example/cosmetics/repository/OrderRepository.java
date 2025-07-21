package com.example.cosmetics.repository;

import com.example.cosmetics.entity.Order;
import com.example.cosmetics.entity.Status;
import com.example.cosmetics.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUser(User user);
    List<Order> findByUserAndStatus(User user, Status status);
}