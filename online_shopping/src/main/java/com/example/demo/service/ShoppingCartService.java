package com.example.demo.service;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.entity.ShoppingCart;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private OrderRepository orderRepository;

    // Get Shopping Cart by Customer ID
    public ShoppingCart getCartByCustomerId(Long customerId) {
        return cartRepository.findByCustomerId(customerId);
    }

    // Add Product to Cart
    public ShoppingCart addProductToCart(Long customerId, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than zero.");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));

        ShoppingCart cart = cartRepository.findByCustomerId(customerId);
        if (cart == null) {
            cart = new ShoppingCart();
            cart.setCustomer(customer);
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

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

        // Ensure the product exists in the cart before attempting to remove it
        if (cart.getItems().containsKey(product)) {
            cart.getItems().remove(product);
        } else {
            throw new RuntimeException("Product not found in the cart.");
        }

        return cartRepository.save(cart);
    }
    
 // Add this method in the ShoppingCartService class
    public void clearCart(Long customerId) {
        ShoppingCart cart = cartRepository.findByCustomerId(customerId);
        if (cart == null) {
            throw new RuntimeException("Cart not found for customer ID: " + customerId);
        }

        // Clear all items from the cart
        cart.getItems().clear();
        cartRepository.save(cart);
    }


    // Checkout Cart
    @Transactional
    public Order checkout(Long customerId) {
        ShoppingCart cart = cartRepository.findByCustomerId(customerId);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty or does not exist for customer ID: " + customerId);
        }

        Order order = new Order();
        order.setCustomer(cart.getCustomer());
        order.setProducts(new HashMap<>(cart.getItems()));
        order.setTotalPrice(cart.getItems().entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum());
        order.setStatus("Pending");

        order = orderRepository.save(order);

        cart.getItems().forEach((product, quantity) -> {
            if (product.getStockQuantity() < quantity) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
            product.setStockQuantity(product.getStockQuantity() - quantity);
            productRepository.save(product);
        });

        cart.getItems().clear();
        cartRepository.save(cart);

        return order;
    }
}
