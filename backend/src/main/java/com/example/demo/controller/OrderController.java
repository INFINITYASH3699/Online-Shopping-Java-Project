package com.example.demo.controller;

import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Get all orders
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();

        // Convert to DTO
        List<Map<String, Object>> response = orders.stream().map(order -> {
            Map<String, Object> orderDTO = new HashMap<>();
            orderDTO.put("id", order.getId());
            orderDTO.put("customer", order.getCustomer());
            orderDTO.put("totalPrice", order.getTotalPrice());
            orderDTO.put("status", order.getStatus());

            Map<Long, Map<String, Object>> productsDTO = new HashMap<>();
            order.getProducts().forEach((product, quantity) -> {
                Map<String, Object> productDetails = new HashMap<>();
                productDetails.put("id", product.getId());
                productDetails.put("name", product.getName());
                productDetails.put("price", product.getPrice());
                productDetails.put("quantity", quantity);
                productsDTO.put(product.getId(), productDetails);
            });

            orderDTO.put("products", productsDTO);
            return orderDTO;
        }).toList();

        return ResponseEntity.ok(response);
    }
    
 // Get an Order by ID
    @GetMapping("/{orderId}")
    public ResponseEntity<Map<String, Object>> getOrderById(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);

        // Convert to DTO
        Map<String, Object> response = new HashMap<>();
        response.put("id", order.getId());
        response.put("customer", order.getCustomer());
        response.put("totalPrice", order.getTotalPrice());
        response.put("status", order.getStatus());

        Map<Long, Map<String, Object>> productsDTO = new HashMap<>();
        order.getProducts().forEach((product, quantity) -> {
            Map<String, Object> productDetails = new HashMap<>();
            productDetails.put("id", product.getId());
            productDetails.put("name", product.getName());
            productDetails.put("price", product.getPrice());
            productDetails.put("quantity", quantity);
            productsDTO.put(product.getId(), productDetails);
        });

        response.put("products", productsDTO);
        return ResponseEntity.ok(response);
    }
    
 // Update Order Status
    @PutMapping("/{orderId}")
    public ResponseEntity<Map<String, Object>> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status) {
        Order updatedOrder = orderService.updateOrderStatus(orderId, status);

        // Convert to DTO
        Map<String, Object> response = new HashMap<>();
        response.put("id", updatedOrder.getId());
        response.put("customer", updatedOrder.getCustomer());
        response.put("totalPrice", updatedOrder.getTotalPrice());
        response.put("status", updatedOrder.getStatus());

        Map<Long, Map<String, Object>> productsDTO = new HashMap<>();
        updatedOrder.getProducts().forEach((product, quantity) -> {
            Map<String, Object> productDetails = new HashMap<>();
            productDetails.put("id", product.getId());
            productDetails.put("name", product.getName());
            productDetails.put("price", product.getPrice());
            productDetails.put("quantity", quantity);
            productsDTO.put(product.getId(), productDetails);
        });

        response.put("products", productsDTO);
        return ResponseEntity.ok(response);
    }


}

