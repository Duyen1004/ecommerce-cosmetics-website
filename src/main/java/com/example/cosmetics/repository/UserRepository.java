package com.example.cosmetics.repository;

import com.example.cosmetics.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    List<User> findAllByStatus(int status); // Lấy danh sách tài khoản theo trạng thái
}
