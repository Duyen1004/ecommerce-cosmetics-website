package com.example.cosmetics.repository;

import com.example.cosmetics.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {
    // Lấy sản phẩm nổi bật (giữ nguyên)
    @Query(value = "SELECT p.* FROM (SELECT *, ROW_NUMBER() OVER (PARTITION BY category_id ORDER BY product_id ASC) AS rn FROM Products WHERE status = 1) p WHERE p.rn = 1", nativeQuery = true)
    List<Product> findFeaturedProducts();

    // Lấy tất cả sản phẩm, bất kể trạng thái
    @Query("SELECT p FROM Product p")
    List<Product> findAll();

    // Lấy sản phẩm theo danh mục, chỉ lấy sản phẩm có status = 1
    @Query("SELECT p FROM Product p WHERE p.category.categoryId = :categoryId AND p.status = 1")
    List<Product> findByCategoryCategoryId(Integer categoryId);

    // Lấy sản phẩm theo ID, không kiểm tra status
    @Query("SELECT p FROM Product p WHERE p.productId = :productId")
    Product findByProductId(Integer productId);

    // Tìm kiếm sản phẩm theo tên, chỉ lấy sản phẩm có status = 1
    @Query("SELECT p FROM Product p WHERE LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) AND p.status = 1")
    List<Product> findByProductNameContainingIgnoreCase(String keyword);
}