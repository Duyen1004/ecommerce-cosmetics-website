package com.example.cosmetics.controller;

import com.example.cosmetics.entity.Order;
import com.example.cosmetics.entity.Payments;
import com.example.cosmetics.entity.Status;
import com.example.cosmetics.entity.User;
import com.example.cosmetics.repository.PaymentRepository;
import com.example.cosmetics.service.OrderService;
import com.example.cosmetics.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.cosmetics.entity.PaymentStatus;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentRepository paymentRepository;

    @GetMapping("/orders")
    public String viewOrders(Authentication authentication,
                             @RequestParam(value = "status", required = false) String status,
                             Model model) {
        try {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            if (user == null) {
                model.addAttribute("error", "Vui lòng đăng nhập để xem lịch sử đơn hàng.");
                return "redirect:/login";
            }

            List<Order> orders;
            if (status != null && !status.isEmpty()) {
                try {
                    Status orderStatus = Status.valueOf(status);
                    orders = orderService.getOrdersByUserAndStatus(user, orderStatus);
                } catch (IllegalArgumentException e) {
                    model.addAttribute("error", "Trạng thái không hợp lệ.");
                    orders = orderService.getOrdersByUser(user);
                }
            } else {
                orders = orderService.getOrdersByUser(user);
            }

            model.addAttribute("orders", orders);
            model.addAttribute("statuses", Arrays.asList(Status.values()));
            return "order-history";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            return "order-history";
        }
    }

    @GetMapping("/orders/{orderId}")
    public String viewOrderDetails(@PathVariable("orderId") int orderId, Authentication authentication, Model model) {
        try {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            if (user == null) {
                model.addAttribute("error", "Vui lòng đăng nhập để xem chi tiết đơn hàng.");
                return "redirect:/login";
            }

            Order order = orderService.getOrderById(orderId);
            if (order == null || order.getUser() == null) {
                model.addAttribute("error", "Không tìm thấy đơn hàng hoặc thông tin người dùng không hợp lệ.");
                return "order-details";
            }

            if (user.getUserId() != order.getUser().getUserId()) {
                model.addAttribute("error", "Bạn không có quyền xem đơn hàng này.");
                return "order-history";
            }

            model.addAttribute("order", order);
            return "order-details";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            return "order-details";
        }
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") int orderId, Authentication authentication, Model model) {
        try {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            if (user == null) {
                model.addAttribute("error", "Vui lòng đăng nhập để hủy đơn hàng.");
                return "redirect:/login";
            }

            Order order = orderService.getOrderById(orderId);
            if (order == null || order.getUser() == null) {
                model.addAttribute("error", "Không tìm thấy đơn hàng.");
                return "redirect:/orders";
            }

            if (user.getUserId() != order.getUser().getUserId()) {
                model.addAttribute("error", "Bạn không có quyền hủy đơn hàng này.");
                return "redirect:/orders";
            }

            if (!order.getStatus().equals(Status.PENDING)) {
                model.addAttribute("error", "Chỉ có thể hủy đơn hàng ở trạng thái Chờ xử lý.");
                return "redirect:/orders";
            }

            order.setStatus(Status.CANCELLED);
            orderService.saveOrder(order);

            List<Payments> payments = order.getPayments();
            if (payments != null && !payments.isEmpty()) {
                for (Payments payment : payments) {
                    payment.setPaymentStatus(PaymentStatus.REFUNDED);
                    paymentRepository.save(payment);
                }
            }

            String message = URLEncoder.encode("Đơn hàng đã được hủy.", StandardCharsets.UTF_8.toString());
            return "redirect:/orders?message=" + message;
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi hủy đơn hàng: " + e.getMessage());
            return "redirect:/orders";
        }
    }
}