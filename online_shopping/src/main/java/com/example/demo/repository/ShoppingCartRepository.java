package com.example.demo.repository;

import com.example.demo.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
	@Query("SELECT sc FROM ShoppingCart sc WHERE sc.customer.id = :customerId")
	ShoppingCart findByCustomerId(@Param("customerId") Long customerId);

}
