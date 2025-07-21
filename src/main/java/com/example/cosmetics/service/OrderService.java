package com.example.cosmetics.service;

import com.example.cosmetics.entity.Cart;
import com.example.cosmetics.entity.Order;
import com.example.cosmetics.entity.Status;
import com.example.cosmetics.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface OrderService {
    Order createOrder(User user, List<Cart> cartItems, String shippingAddress, String paymentMethod) throws Exception;
    List<Order> getOrdersByUser(User user);
    List<Order> getOrdersByUserAndStatus(User user, Status status);
    Order getOrderById(int orderId);
    Order saveOrder(Order order);
    List<Order> findAllOrders();
}