package com.example.demo.repo;
import java.util.*;
import com.example.demo.models.User;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.models.CartItem;
import com.example.demo.models.Product;
public interface CartItemRepo extends JpaRepository<CartItem,Long> {
    //find all the cart items that belongs to a user
    List<CartItem> findByUser(User user);
    // findby user and product
    Optional<CartItem> findByUserAndProduct(User user, Product product);
    // findby cart item by user
    Optional<CartItem> findByUserAndId(User user, Long id);
    
    long countByUser(User user);
    
    void deleteByUser(User user);
    void deleteByUserAndId(User user, Long id);

    //Empty Cart after purchase
    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem c WHERE c.user.id = :userId")
    void deleteAllByUserId(Long userId);

}   
