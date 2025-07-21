package com.example.cosmetics.controller;

import com.example.cosmetics.entity.Categories;
import com.example.cosmetics.entity.Order;
import com.example.cosmetics.entity.Product;
import com.example.cosmetics.entity.Status;
import com.example.cosmetics.entity.User;
import com.example.cosmetics.service.CategoryService;
import com.example.cosmetics.service.OrderService;
import com.example.cosmetics.service.ProductService;
import com.example.cosmetics.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private OrderService orderService;

    @GetMapping("")
    public String admin() {
        return "admin";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userService.findAllByStatus(1);
        model.addAttribute("users", users);
        return "admin-users";
    }

    @PostMapping("/users/{id}/status")
    public String changeUserStatus(@PathVariable("id") int userId, Model model) {
        try {
            User user = userService.findById(userId);
            if (user != null) {
                user.setStatus(user.getStatus() == 1 ? 0 : 1);
                userService.updateUser(user);
            }
        } catch (Exception e) {
            model.addAttribute("error", "Không thể cập nhật trạng thái: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/products")
    public String listProducts(Model model) {
        List<Product> products = productService.findAllProducts();
        model.addAttribute("products", products);
        return "admin-products";
    }

    @GetMapping("/products/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin-product-add";
    }

    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute("product") Product product, Model model) {
        try {
            product.setCreateAt(LocalDate.now());
            product.setStatus(1);
            productService.save(product);
        } catch (Exception e) {
            model.addAttribute("error", "Không thể thêm sản phẩm: " + e.getMessage());
            model.addAttribute("categories", categoryService.getAllCategories());
            return "admin-product-add";
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/products/edit/{id}")
    public String showEditProductForm(@PathVariable("id") int productId, Model model) {
        Product product = productService.findById(productId);
        if (product == null) {
            model.addAttribute("error", "Sản phẩm không tồn tại");
            return "redirect:/admin/products";
        }
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin-product-edit";
    }

    @PostMapping("/products/edit/{id}")
    public String editProduct(@PathVariable("id") int productId, @ModelAttribute("product") Product product, Model model) {
        try {
            Product existingProduct = productService.findById(productId);
            if (existingProduct == null) {
                model.addAttribute("error", "Sản phẩm không tồn tại");
                return "redirect:/admin/products";
            }
            product.setProductId(productId);
            product.setCreateAt(existingProduct.getCreateAt());
            product.setStatus(existingProduct.getStatus());
            productService.save(product);
        } catch (Exception e) {
            model.addAttribute("error", "Không thể cập nhật sản phẩm: " + e.getMessage());
            model.addAttribute("categories", categoryService.getAllCategories());
            return "admin-product-edit";
        }
        return "redirect:/admin/products";
    }

    @PostMapping("/products/{id}/status")
    public String changeProductStatus(@PathVariable("id") int productId, Model model) {
        try {
            Product product = productService.findById(productId);
            if (product != null) {
                product.setStatus(product.getStatus() == 1 ? 0 : 1);
                productService.save(product);
            }
        } catch (Exception e) {
            model.addAttribute("error", "Không thể cập nhật trạng thái: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/orders")
    public String listOrders(Model model) {
        List<Order> orders = orderService.findAllOrders();
        model.addAttribute("orders", orders);
        model.addAttribute("statusList", Status.values()); // Thêm danh sách trạng thái
        return "admin-orders";
    }

    @PostMapping("/orders/{id}/status")
    public String changeOrderStatus(@PathVariable("id") int orderId, @RequestParam("status") String status, Model model) {
        try {
            Order order = orderService.getOrderById(orderId);
            if (order != null) {
                order.setStatus(Status.valueOf(status));
                orderService.saveOrder(order);
            }
        } catch (Exception e) {
            model.addAttribute("error", "Không thể cập nhật trạng thái đơn hàng: " + e.getMessage());
        }
        return "redirect:/admin/orders";
    }

    @GetMapping("/orders/details/{id}")
    public String viewOrderDetails(@PathVariable("id") int orderId, Model model) {
        try {
            Order order = orderService.getOrderById(orderId);
            if (order == null) {
                model.addAttribute("error", "Đơn hàng không tồn tại");
                return "redirect:/admin/orders";
            }
            model.addAttribute("order", order);
            return "admin-order-details";
        } catch (Exception e) {
            model.addAttribute("error", "Không thể tải chi tiết đơn hàng: " + e.getMessage());
            return "redirect:/admin/orders";
        }
    }
}