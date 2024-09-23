package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;
import com.example.demo.models.Orders;
import com.example.demo.models.User;

public interface OrderRepo extends JpaRepository<Orders, Long> {
    @Query("SELECT o FROM Orders o")
    List<Orders> findByUser(User user);
    Optional<Orders> findByUserAndId(User user, Long id);
    
    long countByUser(User user);
    
    
}
