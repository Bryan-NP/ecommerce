package com.example.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.models.OrderItem;

import jakarta.transaction.Transactional;

public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {
   
    @Modifying
    @Transactional
    // Compare the order's ID (o.order.id) with the provided orderId
    @Query("SELECT o FROM OrderItem o WHERE o.order.id = :orderId")
    List<OrderItem> findByOrder(@Param("orderId") Long orderId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM OrderItem oi WHERE oi.order.id = :orderId")
    void deleteByOrderId(@Param("orderId") Long orderId);

}
