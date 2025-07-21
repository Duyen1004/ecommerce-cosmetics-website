package com.example.cosmetics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOrderConfirmationEmail(String to, String orderId, String totalPrice, String shippingAddress, String paymentMethod) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("Xác nhận đơn hàng #" + orderId);
        helper.setFrom("duyenhthe181489@fpt.edu.vn"); // Thay bằng email đã cấu hình

        String htmlContent = "<h3>Cảm ơn bạn đã mua sắm tại Cosmetics!</h3>" +
                "<p>Đơn hàng của bạn đã được đặt thành công. Dưới đây là thông tin chi tiết:</p>" +
                "<ul>" +
                "<li><strong>Mã đơn hàng:</strong> " + orderId + "</li>" +
                "<li><strong>Tổng tiền:</strong> " + totalPrice + " ₫</li>" +
                "<li><strong>Địa chỉ giao hàng:</strong> " + shippingAddress + "</li>" +
                "<li><strong>Phương thức thanh toán:</strong> " + paymentMethod + "</li>" +
                "</ul>" +
                "<p>Vui lòng kiểm tra thông tin đơn hàng của bạn tại <a href='http://localhost:8082/home/orders'>Lịch sử đơn hàng</a>.</p>" +
                "<p>Trân trọng,<br/>Cosmetics Team</p>";

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}