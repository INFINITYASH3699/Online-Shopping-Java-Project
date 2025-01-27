package com.example.demo.service;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.entity.ShoppingCart;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ShoppingCartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
public class ShoppingCartService {

    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartService.class);

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
        logger.info("Fetching cart for customer ID: {}", customerId);
        ShoppingCart cart = cartRepository.findByCustomerId(customerId);
        if (cart == null) {
            throw new RuntimeException("Cart not found for customer ID: " + customerId);
        }
        logger.info("Cart retrieved: {}", cart.getItems());
        return cart;
    }

    // Add Product to Cart (Updated Logic)
    public ShoppingCart addProductToCart(Long customerId, Long productId, int quantity) {
        logger.info("Updating quantity for product ID: {} to {} for customer ID: {}", productId, quantity, customerId);

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

        // Set quantity directly instead of incrementing
        cart.getItems().put(product, quantity);

        ShoppingCart updatedCart = cartRepository.save(cart);
        logger.info("Product quantity updated successfully. Cart items: {}", updatedCart.getItems());
        return updatedCart;
    }

    // Remove Product from Cart
    public ShoppingCart removeProductFromCart(Long customerId, Long productId) {
        logger.info("Removing product ID: {} from customer ID: {}'s cart.", productId, customerId);

        ShoppingCart cart = cartRepository.findByCustomerId(customerId);
        if (cart == null) {
            throw new RuntimeException("Cart not found for customer ID: " + customerId);
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        if (cart.getItems().containsKey(product)) {
            cart.getItems().remove(product);
            ShoppingCart updatedCart = cartRepository.save(cart);
            logger.info("Product removed successfully. Updated cart items: {}", updatedCart.getItems());
            return updatedCart;
        } else {
            throw new RuntimeException("Product not found in the cart.");
        }
    }

    // Clear the Cart
    public void clearCart(Long customerId) {
        logger.info("Clearing cart for customer ID: {}", customerId);
        ShoppingCart cart = cartRepository.findByCustomerId(customerId);
        if (cart == null) {
            throw new RuntimeException("Cart not found for customer ID: " + customerId);
        }

        cart.getItems().clear();
        cartRepository.save(cart);
        logger.info("Cart cleared successfully for customer ID: {}", customerId);
    }

    // Checkout Cart
    @Transactional
    public Order checkout(Long customerId) {
        logger.info("Checking out cart for customer ID: {}", customerId);

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

        logger.info("Checkout completed successfully for customer ID: {}", customerId);
        return order;
    }
}
