package com.example.cosmetics.controller;

import com.example.cosmetics.dto.UserDTO;
import com.example.cosmetics.entity.User;
import com.example.cosmetics.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String showProfile(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(email);
        if (user == null) {
            model.addAttribute("error", "Không tìm thấy thông tin người dùng!");
            return "login";
        }
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/profile/edit")
    public String showEditProfileForm(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(email);
        if (user == null) {
            model.addAttribute("error", "Không tìm thấy thông tin người dùng!");
            return "login";
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setLastName(user.getLastName());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setAddress(user.getAddress());
        userDTO.setGender(user.getGender());
        userDTO.setDob(user.getDob());
        model.addAttribute("user", userDTO);
        return "edit-profile";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("error", "Vui lòng kiểm tra lại thông tin nhập vào.");
            return "edit-profile";
        }

        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User existingUser = userService.findByEmail(email);
            if (existingUser == null) {
                model.addAttribute("error", "Không tìm thấy thông tin người dùng!");
                return "edit-profile";
            }

            existingUser.setLastName(userDTO.getLastName());
            existingUser.setFirstName(userDTO.getFirstName());
            existingUser.setPhone(userDTO.getPhone());
            existingUser.setAddress(userDTO.getAddress());
            existingUser.setGender(userDTO.getGender());
            existingUser.setDob(userDTO.getDob());
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                String pattern = "^[A-Z](?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]|:;\"'<>,.?/]).{7,}$";
                if (!userDTO.getPassword().matches(pattern)) {
                    result.rejectValue("password", "error.user", "Mật khẩu phải bắt đầu bằng chữ hoa, chứa ít nhất 1 chữ thường, 1 số, 1 ký tự đặc biệt và có ít nhất 8 ký tự!");
                    return "edit-profile";
                }

                existingUser.setPassword(userDTO.getPassword());
            }
            existingUser.setUpdatedAt(java.time.LocalDate.now());
            userService.updateUser(existingUser);

            model.addAttribute("message", "Cập nhật hồ sơ thành công!");
            model.addAttribute("user", existingUser);
            return "profile";
        } catch (Exception e) {
            model.addAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
            return "edit-profile";
        }
    }
}