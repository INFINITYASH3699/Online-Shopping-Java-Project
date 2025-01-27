package com.example.demo.controller;

import com.example.demo.entity.Order;
import com.example.demo.entity.ShoppingCart;
import com.example.demo.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/carts")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService cartService;

    // Get Cart by Customer ID
    @GetMapping("/{customerId}")
    public ResponseEntity<Map<String, Object>> getCart(@PathVariable Long customerId) {
        ShoppingCart cart = cartService.getCartByCustomerId(customerId);
        return ResponseEntity.ok(convertToDTO(cart));
    }

    // Add Product to Cart
    @PostMapping("/{customerId}/add")
    public ResponseEntity<Map<String, Object>> addProductToCart(
            @PathVariable Long customerId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        ShoppingCart cart = cartService.addProductToCart(customerId, productId, quantity);
        return ResponseEntity.ok(convertToDTO(cart));
    }

    // Remove Product from Cart
    @DeleteMapping("/{customerId}/remove")
    public ResponseEntity<Map<String, Object>> removeProductFromCart(
            @PathVariable Long customerId,
            @RequestParam Long productId) {
        ShoppingCart cart = cartService.removeProductFromCart(customerId, productId);
        return ResponseEntity.ok(convertToDTO(cart));
    }
    
    @PostMapping("/{customerId}/update")
    public ResponseEntity<Map<String, Object>> updateProductQuantity(
            @PathVariable Long customerId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        // Use the existing service method to set the exact quantity
        ShoppingCart cart = cartService.updateProductQuantity(customerId, productId, quantity);
        return ResponseEntity.ok(convertToDTO(cart));
    }


    // Clear Cart
    @DeleteMapping("/{customerId}/clear")
    public ResponseEntity<String> clearCart(@PathVariable Long customerId) {
        cartService.clearCart(customerId);
        return ResponseEntity.ok("Cart cleared successfully!");
    }
    
 // Checkout Endpoint
    @PostMapping("/{customerId}/checkout")
    public ResponseEntity<Map<String, Object>> checkout(@PathVariable Long customerId) {
        Order order = cartService.checkout(customerId);

        // Convert the order to a simplified response DTO
        Map<String, Object> response = new HashMap<>();
        response.put("orderId", order.getId());
        response.put("customer", order.getCustomer());
        response.put("totalPrice", order.getTotalPrice());
        response.put("status", order.getStatus());

        Map<Long, Map<String, Object>> orderedItems = new HashMap<>();
        order.getProducts().forEach((product, quantity) -> {
            Map<String, Object> productDetails = new HashMap<>();
            productDetails.put("id", product.getId());
            productDetails.put("name", product.getName());
            productDetails.put("price", product.getPrice());
            productDetails.put("quantity", quantity);
            orderedItems.put(product.getId(), productDetails);
        });

        response.put("orderedItems", orderedItems);
        return ResponseEntity.ok(response);
    }


    // Helper Method to Convert ShoppingCart to DTO
    private Map<String, Object> convertToDTO(ShoppingCart cart) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", cart.getId());
        response.put("customer", cart.getCustomer());

        Map<Long, Map<String, Object>> itemsMap = new HashMap<>();
        cart.getItems().forEach((product, quantity) -> {
            Map<String, Object> productDetails = new HashMap<>();
            productDetails.put("id", product.getId());
            productDetails.put("name", product.getName());
            productDetails.put("price", product.getPrice());
            productDetails.put("quantity", quantity);
            itemsMap.put(product.getId(), productDetails);
        });

        response.put("items", itemsMap);
        return response;
    }
}
