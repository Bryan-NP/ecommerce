package com.example.demo.services;
import jakarta.transaction.Transactional;
import java.util.*;
import org.springframework.stereotype.Service;

import com.example.demo.models.CartItem;
import com.example.demo.models.Product;
import com.example.demo.models.User;
import com.example.demo.repo.CartItemRepo;

@Service
public class CartItemServices {
    private final CartItemRepo cartItemRepo;

    public CartItemServices(CartItemRepo cartItemRepo){
        this.cartItemRepo = cartItemRepo;
    }
    // when a method is transactional, if throws exception for any reason
    // All database writes and updates will be undo.
    @Transactional
    public CartItem addToCart(User user, Product product, int quantity){
        // if the product already exist in the cart vs the product not in cart
        Optional<CartItem> existingItem = cartItemRepo.findByUserAndProduct(user, product);
        if (existingItem.isPresent()){
            //Do something
            //we have to use .get()
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            return cartItemRepo.save(cartItem);
        }else{
            //if product is not in the cart
            CartItem newItem = new CartItem(user,product,quantity);
            return cartItemRepo.save(newItem);
        }
    }

    public List<CartItem> findByUser(User user){
        // todo: for further business logic
        // ex: recommendations, discount code, out of stock notice, price change
        return cartItemRepo.findByUser(user);

    }

    @Transactional
    public void updateQuantity(long cartItemId, User user, int newQuantity){
        CartItem existingItem = cartItemRepo.findByUserAndId(user,cartItemId)
                .orElseThrow( () -> new IllegalArgumentException("No Cart Item with that id exists"));
        existingItem.setQuantity(newQuantity);
        cartItemRepo.save(existingItem);

    }

    @Transactional
    public void removeFromCart(long cartItemId, User user){
        cartItemRepo.deleteByUserAndId(user,cartItemId);
    }
}
