package com.example.cosmetics.controller;

import com.example.cosmetics.dto.UserDTO;
import com.example.cosmetics.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserDTO());
        model.addAttribute("success", false);
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("error", "Vui lòng kiểm tra lại thông tin nhập vào.");
            model.addAttribute("success", false);
            return "register";
        }

        try {
            userService.registerUser(userDTO);
            model.addAttribute("message", "Đăng ký thành công!");
            model.addAttribute("success", true);
            model.addAttribute("user", new UserDTO()); // Làm mới form
            return "register";
        } catch (Exception e) {
            model.addAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
            model.addAttribute("success", false);
            return "register";
        }
    }
}