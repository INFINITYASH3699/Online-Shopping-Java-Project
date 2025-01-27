package com.example.demo.dto;

import java.util.Map;

import com.example.demo.entity.Customer;

public class ShoppingCartDTO {
    private Long id;
    private Customer customer;
    private Map<Long, ItemDetail> items; // Map<ProductId, ItemDetail>

    // Inner class to represent item details
    public static class ItemDetail {
        private String productName;
        private Double productPrice;
        private Integer quantity;

        // Getters and setters
        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public Double getProductPrice() {
            return productPrice;
        }

        public void setProductPrice(Double productPrice) {
            this.productPrice = productPrice;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Map<Long, ItemDetail> getItems() {
        return items;
    }

    public void setItems(Map<Long, ItemDetail> items) {
        this.items = items;
    }
}
