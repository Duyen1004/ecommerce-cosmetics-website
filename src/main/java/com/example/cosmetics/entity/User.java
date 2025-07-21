package com.example.cosmetics.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "[user]")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @ManyToOne
    @JoinColumn(name = "roleId", nullable = false)
    private Role role;

    @Column(nullable = false, length = 100, columnDefinition = "NVARCHAR(100)")
    private String firstName;

    @Column(nullable = false, length = 100, columnDefinition = "NVARCHAR(100)")
    private String lastName;

    @Column(nullable = false, length = 100, columnDefinition = "NVARCHAR(100)", unique = true)
    private String email;

    @Column(nullable = false, length = 255, columnDefinition = "NVARCHAR(255)")
    private String password;

    @Column(nullable = false, length = 20, columnDefinition = "NVARCHAR(20)")
    private String phone;

    @Column(nullable = false, length = 500, columnDefinition = "NVARCHAR(500)")
    private String address;

    @Column(nullable = false, length = 10, columnDefinition = "NVARCHAR(10) CHECK (gender IN (N'Nam', N'Nữ', N'Khác'))")
    private String gender;

    @Column(nullable = false, columnDefinition = "DATE")
    private LocalDate dob;

    @Column(columnDefinition = "DATE DEFAULT GETDATE()")
    private LocalDate createAt;

    @Column(columnDefinition = "DATE DEFAULT GETDATE()")
    private LocalDate updatedAt;

    @Column(nullable = false)
    private int status;

    public User() {
    }

    public User(int userId, Role role, String firstName, String lastName, String email, String password, String phone, String address, String gender, LocalDate dob, LocalDate createAt, LocalDate updatedAt, int status) {
        this.userId = userId;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.dob = dob;
        this.createAt = createAt;
        this.updatedAt = updatedAt;
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
