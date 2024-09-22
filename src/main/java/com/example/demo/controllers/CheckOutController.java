package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.models.User;
import com.example.demo.services.CartItemServices;
import com.example.demo.services.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

@Controller
@RequestMapping("/checkout")
public class CheckOutController {
    private final StripeService stripeService;
    private final CartItemServices cartItemServices;
    
    @Autowired
    public CheckOutController(StripeService stripeService, CartItemServices cartItemServices){
        this.stripeService = stripeService;
        this.cartItemServices = cartItemServices;
    }

    @GetMapping("/create-checkout-session")
    public String createCheckoutSession(
        @AuthenticationPrincipal User user,
        Model model
    ){
        try{
            String successUrl = "https://www.google.com";
            String cancelUrl = "https://www.yahoo.com";
            var cartItems = cartItemServices.findByUser(user);
            Session session = stripeService.createCheckoutSession(cartItems,user.getId(),successUrl,cancelUrl);
            model.addAttribute("sessionId", session.getId());
            model.addAttribute("stripePublicKey", stripeService.getPublicKey());
            return "checkout/checkout";

        }catch (StripeException e){
            model.addAttribute("error",e.getMessage());
            return "error";
        }
        
        
    }
    
}
