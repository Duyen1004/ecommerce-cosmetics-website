package com.example.cosmetics.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    private Categories category;

    @Column(nullable = false, length = 150, columnDefinition = "NVARCHAR(150)")
    private String productName;

    @Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String specifications;

    @Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String usage;

    @Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String ingredients;

    @Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String capacity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String img;

    @Column(columnDefinition = "DATE DEFAULT GETDATE()")
    private LocalDate createAt;

    @Column(nullable = false)
    private int status;

    public Product() {
    }

    public Product(int productId, Categories category, String productName, String description, String specifications, String usage, String ingredients, String capacity, BigDecimal price, int quantity, String img, LocalDate createAt, int status) {
        this.productId = productId;
        this.category = category;
        this.productName = productName;
        this.description = description;
        this.specifications = specifications;
        this.usage = usage;
        this.ingredients = ingredients;
        this.capacity = capacity;
        this.price = price;
        this.quantity = quantity;
        this.img = img;
        this.createAt = createAt;
        this.status = status;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
