package com.example.demo.repository;

import com.example.demo.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for accessing Order entities.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    // We can add custom query methods if needed, but basic CRUD comes from JpaRepository
}
