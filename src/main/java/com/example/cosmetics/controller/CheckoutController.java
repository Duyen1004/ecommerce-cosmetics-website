package com.example.cosmetics.controller;

import com.example.cosmetics.entity.*;
import com.example.cosmetics.repository.PaymentRepository;
import com.example.cosmetics.service.CartService;
import com.example.cosmetics.service.EmailService;
import com.example.cosmetics.service.OrderService;
import com.example.cosmetics.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.http.HttpSession;
@Controller
public class CheckoutController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;
    @Autowired
    private EmailService emailService;

    @Autowired
    private PaymentRepository paymentRepository; // Thêm repository để lưu Payments

    @Autowired
    private PayOS payOS;

    @GetMapping("/checkout")
    public String showCheckout(Authentication authentication, Model model) {
        try {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            if (user == null) {
                model.addAttribute("error", "Vui lòng đăng nhập để thanh toán.");
                return "redirect:/login";
            }
            List<Cart> cartItems = cartService.getCartByUser(user);
            if (cartItems.isEmpty()) {
                model.addAttribute("error", "Giỏ hàng của bạn đang trống.");
                return "redirect:/cart";
            }
            double totalPrice = cartItems.stream()
                    .filter(cart -> cart.getProduct() != null && cart.getProduct().getPrice() != null)
                    .mapToDouble(cart -> cart.getProduct().getPrice()
                            .multiply(BigDecimal.valueOf(cart.getQuantity()))
                            .doubleValue())
                    .sum();
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("user", user);
            return "checkout";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            return "checkout";
        }
    }

    @PostMapping("/checkout")
    public String processCheckout(@RequestParam("shippingAddress") String shippingAddress,
                                  @RequestParam("paymentMethod") String paymentMethod,
                                  Authentication authentication,
                                  Model model, HttpSession session) {
        try {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            if (user == null) {
                model.addAttribute("error", "Vui lòng đăng nhập để thanh toán.");
                return "redirect:/login";
            }

            List<Cart> cartItems = cartService.getCartByUser(user);
            if (cartItems.isEmpty()) {
                model.addAttribute("error", "Giỏ hàng của bạn đang trống.");
                return "redirect:/cart";
            }

            // Tạo đơn hàng
            Order order = orderService.createOrder(user, cartItems, shippingAddress, paymentMethod);
            session.setAttribute("orderId", order.getOrderId()); // Lưu orderId vào session
            if ("BANK_TRANSFER".equals(paymentMethod)) {
                // Tạo dữ liệu thanh toán (Dữ liệu thanh toán - PaymentData)
                List<ItemData> items = new ArrayList<>();
                for (Cart cart : cartItems) {
                    items.add(ItemData.builder()
                            .name(cart.getProduct().getProductName())
                            .quantity(cart.getQuantity())
                            .price(cart.getProduct().getPrice().intValue())
                            .build());
                }

                long totalAmount = cartItems.stream()
                        .filter(cart -> cart.getProduct() != null && cart.getProduct().getPrice() != null)
                        .mapToLong(cart -> cart.getProduct().getPrice()
                                .multiply(BigDecimal.valueOf(cart.getQuantity()))
                                .longValue())
                        .sum();

                PaymentData paymentData = PaymentData.builder()
                        .orderCode((long) order.getOrderId())
                        .amount((int) totalAmount)
                        .description("Thanh toán đơn hàng #" + order.getOrderId())
                        .items(items)
                        .returnUrl("http://localhost:8082/home/checkout/success")
                        .cancelUrl("http://localhost:8082/home/checkout/cancel")
                        .build();

                // Gọi API PayOS để tạo liên kết thanh toán
                CheckoutResponseData checkoutResponseData = payOS.createPaymentLink(paymentData);

                // Lưu transactionId vào Payments
                Payments payment = paymentRepository.findByOrder(order)
                        .orElseThrow(() -> new Exception("Không tìm thấy bản ghi thanh toán cho đơn hàng"));
                payment.setTransactionId(checkoutResponseData.getCheckoutUrl());
                payment.setPaymentStatus(checkoutResponseData.getStatus().equals("PENDING") ? PaymentStatus.PENDING : PaymentStatus.FAILED);
                paymentRepository.save(payment);

                // Xóa giỏ hàng sau khi tạo liên kết thanh toán
                cartService.clearCart(user);

                // Chuyển hướng đến liên kết thanh toán
                return "redirect:" + checkoutResponseData.getCheckoutUrl();
            } else {
                // Xử lý COD
                cartService.clearCart(user);
                return "redirect:/checkout/success";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi xử lý thanh toán: " + e.getMessage());
            return "checkout";
        }
    }

    @GetMapping("/checkout/success")
    public String checkoutSuccess(Model model, HttpSession session) {
        try {
            Integer orderId = (Integer) session.getAttribute("orderId");
            if (orderId != null) {
                Order order = orderService.getOrderById(orderId);
                if (order != null) {
                    String formattedTotalPrice = String.format("%,.0f", order.getTotalPrice().doubleValue());
                    emailService.sendOrderConfirmationEmail(
                            order.getUser().getEmail(),
                            String.valueOf(order.getOrderId()),
                            formattedTotalPrice,
                            order.getShippingAddress(),
                            order.getPaymentMethod().toString()
                    );
                    session.removeAttribute("orderId"); // Xóa orderId sau khi sử dụng
                }
            }
            model.addAttribute("message", "Thanh toán thành công!");
            return "checkout-success";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi gửi email xác nhận: " + e.getMessage());
            return "checkout-error";
        }
    }

    @GetMapping("/checkout/cancel")
    public String checkoutCancel(Model model) {
        model.addAttribute("error", "Thanh toán của bạn đã bị hủy. Vui lòng thử lại.");
        return "checkout-error"; // Redirect to the new error page
    }
}