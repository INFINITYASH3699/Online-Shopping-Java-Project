package com.example.demo.service;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Product;
import com.example.demo.entity.ShoppingCart;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ShoppingCartService {

    @Autowired
    private ShoppingCartRepository cartRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    // Get Shopping Cart by Customer ID
    public ShoppingCart getCartByCustomerId(Long customerId) {
        ShoppingCart cart = cartRepository.findByCustomerId(customerId);
        if (cart == null) {
            throw new RuntimeException("Cart not found for customer ID: " + customerId);
        }
        return cart;
    }

    // Add Product to Cart
    public ShoppingCart addProductToCart(Long customerId, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than zero.");
        }

        // Find the customer
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));

        // Find or create the shopping cart
        ShoppingCart cart = cartRepository.findByCustomerId(customerId);
        if (cart == null) {
            cart = new ShoppingCart();
            cart.setCustomer(customer);
            cart = cartRepository.save(cart);
        }

        // Find the product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        // Update the cart items
        cart.getItems().put(product, cart.getItems().getOrDefault(product, 0) + quantity);

        return cartRepository.save(cart);
    }

    // Remove Product from Cart
    public ShoppingCart removeProductFromCart(Long customerId, Long productId) {
        ShoppingCart cart = cartRepository.findByCustomerId(customerId);
        if (cart == null) {
            throw new RuntimeException("Cart not found for customer ID: " + customerId);
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        cart.getItems().remove(product);

        return cartRepository.save(cart);
    }

    // Clear the Cart
    public void clearCart(Long customerId) {
        ShoppingCart cart = cartRepository.findByCustomerId(customerId);
        if (cart != null) {
            cart.getItems().clear();
            cartRepository.save(cart);
        }
    }

    // Convert ShoppingCart to DTO for clean API responses
    public Map<String, Object> convertToDTO(ShoppingCart cart) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", cart.getId());
        response.put("customer", cart.getCustomer());
        Map<Long, Map<String, Object>> itemsMap = new HashMap<>();
        cart.getItems().forEach((product, quantity) -> {
            Map<String, Object> itemDetails = new HashMap<>();
            itemDetails.put("name", product.getName());
            itemDetails.put("price", product.getPrice());
            itemDetails.put("quantity", quantity);
            itemsMap.put(product.getId(), itemDetails);
        });
        response.put("items", itemsMap);
        return response;
    }
}
