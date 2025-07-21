package com.example.cosmetics.service.impl;

import com.example.cosmetics.entity.*;
import com.example.cosmetics.repository.OrderDetailRepository;
import com.example.cosmetics.repository.OrderRepository;
import com.example.cosmetics.repository.PaymentRepository;
import com.example.cosmetics.repository.ProductRepository;
import com.example.cosmetics.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional
    public Order createOrder(User user, List<Cart> cartItems, String shippingAddress, String paymentMethod) throws Exception {
        if (user == null) {
            throw new Exception("Thông tin người dùng không hợp lệ.");
        }

        if (cartItems.isEmpty()) {
            throw new Exception("Giỏ hàng trống, không thể tạo đơn hàng.");
        }

        // Kiểm tra số lượng tồn kho
        for (Cart cart : cartItems) {
            Product product = cart.getProduct();
            if (product.getQuantity() < cart.getQuantity()) {
                throw new Exception("Sản phẩm " + product.getProductName() + " không đủ số lượng trong kho.");
            }
        }

        // Tính tổng giá
        double totalPrice = cartItems.stream()
                .filter(cart -> cart.getProduct() != null && cart.getProduct().getPrice() != null)
                .mapToDouble(cart -> cart.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(cart.getQuantity()))
                        .doubleValue())
                .sum();

        // Tạo đơn hàng
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDate.now());
        order.setTotalPrice(BigDecimal.valueOf(totalPrice));
        order.setStatus(Status.PENDING);
        order.setShippingAddress(shippingAddress);
        order.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));
        orderRepository.save(order);

        // Tạo chi tiết đơn hàng và cập nhật số lượng sản phẩm
        for (Cart cart : cartItems) {
            Product product = cart.getProduct();
            product.setQuantity(product.getQuantity() - cart.getQuantity());
            productRepository.save(product);

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(cart.getQuantity());
            orderDetail.setPrice(product.getPrice());
            orderDetailRepository.save(orderDetail);
        }

        // Tạo bản ghi thanh toán
        Payments payment = new Payments();
        payment.setOrder(order);
        payment.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));
        payment.setPaymentAmount(BigDecimal.valueOf(totalPrice));
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setPaymentDate(LocalDateTime.now());
        paymentRepository.save(payment);

        return order;
    }


    @Override
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    @Override
    public List<Order> getOrdersByUserAndStatus(User user, Status status) {
        return orderRepository.findByUserAndStatus(user, status);
    }

    @Override
    public Order getOrderById(int orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng với ID: " + orderId));
    }

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }
}