package com.example.cosmetics.dto;

import com.example.cosmetics.controller.RegisterController;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.time.LocalDate;

public class UserDTO {
    @NotBlank(message = "Họ không được để trống")
    @Size(min = 2, max = 100, message = "Họ phải có từ 2 đến 100 ký tự.")
    @Pattern(regexp = "^[a-zA-ZÀ-ỹ\\s]*$", message = "Họ không được chứa kí tự đặc biệt.")
    private String lastName;

    @NotBlank(message = "Tên không được để trống")
    @Size(min = 2, max = 100, message = "Tên phải có từ 2 đến 100 ký tự.")
    @Pattern(regexp = "^[a-zA-ZÀ-ỹ\\s]*$", message = "Tên không được chứa kí tự đặc biệt.")
    private String firstName;

    @NotBlank(message = "Email không được để trống")
    @Size(min = 5, max = 100, message = "Email phải có từ 5 đến 100 ký tự.")
    @Email(message = "Email phải có định dạng hợp lệ.")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Size(min = 10, max = 10, message = "Số điện thoại phải có đúng 10 chữ số.")
    @Pattern(regexp = "^0[0-9]{9}$", message = "Số điện thoại phải bắt đầu bằng số 0 và có đúng 10 chữ số.")
    private String phone;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(min = 5, max = 500, message = "Địa chỉ phải có từ 5 đến 500 ký tự.")
    @Pattern(regexp = "^[a-zA-ZÀ-ỹ0-9,\\s.-]*$", message = "Địa chỉ không được chứa ký tự đặc biệt.")
    private String address;

    @NotBlank(message = "Giới tính không được để trống")
    @Pattern(regexp = "Nam|Nữ|Khác", message = "Giới tính phải là 'Nam', 'Nữ', hoặc 'Khác'.")
    private String gender;

    @NotNull(message = "Ngày sinh không được để trống")
    @Past(message = "Ngày sinh phải là ngày trong quá khứ.")
    @DateTimeFormat(pattern = "yyyy-MM-dd") // Đảm bảo khớp với input type="date"
    private LocalDate dob;

    @NotBlank(message = "Mật khẩu không được để trống",groups = RegisterController.class)
    @Pattern(
            regexp = "^[A-Z](?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]|:;\"'<>,.?/]).{7,}$",
            message = "Mật khẩu phải bắt đầu bằng chữ hoa, chứa ít nhất 1 chữ thường, 1 số, 1 ký tự đặc biệt và có ít nhất 8 ký tự!", groups = RegisterController.class
    )
    private String password;
    private LocalDate createAt;
    private LocalDate updatedAt;

    public UserDTO() {
    }

    public UserDTO(String lastName, String firstName, String email, String phone, String address, String gender, LocalDate dob, String password, LocalDate createAt, LocalDate updatedAt) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.dob = dob;
        this.password = password;
        this.createAt = createAt;
        this.updatedAt = updatedAt;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}