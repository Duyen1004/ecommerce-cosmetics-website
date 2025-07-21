package com.example.cosmetics.repository;

import com.example.cosmetics.entity.Order;
import com.example.cosmetics.entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payments, Integer> {
    Optional<Payments> findByOrder(Order order);
}