package com.example.cosmetics.service;

import com.example.cosmetics.dto.UserDTO;
import com.example.cosmetics.entity.User;

import java.util.List;

public interface UserService {
    void registerUser(UserDTO userDTO) throws Exception;
    User findByEmail(String email);
    User findById(int id);
    void updateUser(User user) throws Exception;
    List<User> findAllByStatus(int status);
    List<User> findAll(); // Added to fetch all users
}