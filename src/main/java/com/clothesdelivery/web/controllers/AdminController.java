package com.clothesdelivery.web.controllers;

import com.clothesdelivery.web.enums.OrderStatus;
import com.clothesdelivery.web.enums.Role;
import com.clothesdelivery.web.repositories.IOrderItemRepository;
import com.clothesdelivery.web.repositories.IOrderRepository;
import com.clothesdelivery.web.repositories.IProductRepository;
import com.clothesdelivery.web.repositories.IUserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {
    @Autowired
    private IProductRepository _products;

    @Autowired
    private IUserRepository _users;

    @Autowired
    private IOrderRepository _orders;

    @GetMapping("/dashboard")
    public String dashboard(@NotNull Model model) {
        var customers = _users.findAllByRole(Role.ROLE_CUSTOMER).size();
        var products = _products.count();

        var orders = _orders.findAll();
        var monthlyOrders = orders.stream().filter(e -> e.getCreatedTime().getMonth() == LocalDate.now().getMonth()).count();
        var pendingOrders = orders.stream().filter(e -> e.getStatus() == OrderStatus.Pendent).count();

        model.addAttribute("customers", customers);
        model.addAttribute("products", products);
        model.addAttribute("monthly_orders", monthlyOrders);
        model.addAttribute("pending_orders", pendingOrders);

        return "admin/dashboard";
    }

    @GetMapping("/products")
    public String products(@NotNull Model model) {
        model.addAttribute("products", _products.findAll());
        return "admin/products";
    }

    @GetMapping("/products/edit/{id}")
    public String productEdit(@PathVariable(value = "id", required = false) Long id) {
        return "admin/product-edit";
    }

    @GetMapping("/users/edit/{id}")
    public String usersEdit(@NotNull @PathVariable("id") Long id) {
        return "admin/users-edit";
    }

    @GetMapping("/users")
    public String users(@NotNull Model model) {
        model.addAttribute("users", _users.findAll().stream().filter(e -> e.getRole().equals(Role.ROLE_ADMIN)).toList());
        return "admin/users";
    }

    @GetMapping("/customers")
    public String customers(@NotNull Model model) {
        model.addAttribute("customers", _users.findAll().stream().filter(e -> e.getRole().equals(Role.ROLE_CUSTOMER)).toList());
        return "admin/customers";
    }
}
