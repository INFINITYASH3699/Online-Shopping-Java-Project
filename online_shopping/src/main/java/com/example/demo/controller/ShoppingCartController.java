package com.example.demo.controller;

import com.example.demo.entity.ShoppingCart;
import com.example.demo.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(cartService.convertToDTO(cart));
    }

    // Add Product to Cart
    @PostMapping("/{customerId}/add")
    public ResponseEntity<Map<String, Object>> addProductToCart(
            @PathVariable Long customerId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        ShoppingCart cart = cartService.addProductToCart(customerId, productId, quantity);
        return ResponseEntity.ok(cartService.convertToDTO(cart));
    }

    // Remove Product from Cart
    @DeleteMapping("/{customerId}/remove")
    public ResponseEntity<Map<String, Object>> removeProductFromCart(
            @PathVariable Long customerId,
            @RequestParam Long productId) {
        ShoppingCart cart = cartService.removeProductFromCart(customerId, productId);
        return ResponseEntity.ok(cartService.convertToDTO(cart));
    }

    // Clear Cart
    @DeleteMapping("/{customerId}/clear")
    public ResponseEntity<String> clearCart(@PathVariable Long customerId) {
        cartService.clearCart(customerId);
        return ResponseEntity.ok("Cart cleared successfully!");
    }
}
