package com.example.cosmetics.service.impl;

import com.example.cosmetics.dto.UserDTO;
import com.example.cosmetics.entity.Role;
import com.example.cosmetics.entity.User;
import com.example.cosmetics.repository.RoleRepository;
import com.example.cosmetics.repository.UserRepository;
import com.example.cosmetics.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void registerUser(UserDTO userDTO) throws Exception {
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new Exception("Email đã tồn tại");
        }
        Role role = roleRepository.findById(2)
                .orElseThrow(() -> new Exception("Role không tồn tại"));
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword()); // Cần mã hóa mật khẩu trong thực tế
        user.setPhone(userDTO.getPhone());
        user.setAddress(userDTO.getAddress());
        user.setGender(userDTO.getGender());
        user.setDob(userDTO.getDob());
        user.setRole(role);
        user.setCreateAt(LocalDate.now());
        user.setUpdatedAt(LocalDate.now());
        user.setStatus(1);
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void updateUser(User user) throws Exception {
        if (userRepository.findById(user.getUserId()).isEmpty()) {
            throw new Exception("Tài khoản không tồn tại");
        }
        user.setUpdatedAt(LocalDate.now());
        userRepository.save(user);
    }

    @Override
    public List<User> findAllByStatus(int status) {
        return userRepository.findAllByStatus(status);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll(); // Fetch all users
    }
}