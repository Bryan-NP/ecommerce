package com.example.demo.controllers;

import com.example.demo.services.CartItemServices;
import com.example.demo.services.ProductService;
import com.example.demo.services.UserService;
import com.example.demo.models.CartItem;
import com.example.demo.models.Product;
import com.example.demo.models.User;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
public class CartItemController {
    private final CartItemServices cartItemServices;
    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public CartItemController(CartItemServices cartItemServices, ProductService productService,
            UserService userService) {
        this.cartItemServices = cartItemServices;
        this.productService = productService;
        this.userService = userService;
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId, @RequestParam(defaultValue = "1") int quantity,
            Principal principal, RedirectAttributes redirectAttributes) {
        // When a route includes the principal in the parameter,
        // the principal will provide information about the currently logged in user.
        try {
            User user = userService.findUserByUsername(principal.getName());
            // Find the product

            Product product = productService.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));
            cartItemServices.addToCart(user, product, quantity);
            redirectAttributes.addFlashAttribute("message",
                    String.format("Added %d %s to cart", quantity, product.getName()));
            return "redirect:/cart";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/products";
        }
    }

    @GetMapping("")
    public String viewCart(Model model, Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        List<CartItem> cartItems = cartItemServices.findByUser(user);
        model.addAttribute("cartItems", cartItems);
        return "cart/index";
    }

    @PostMapping("/{cartItemId}/updateQuantity")
    public String updateQuantity(@PathVariable long cartItemId,
            @RequestParam int newQuantity, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findUserByUsername(principal.getName());
            cartItemServices.updateQuantity(cartItemId, user, newQuantity);
            redirectAttributes.addFlashAttribute("message", "Quantity updated");
            return "redirect:/cart";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("Error", e.getMessage());
            return "redirect:/cart";
        }

    }

    @GetMapping("/{cartItemId}/remove")
    public String removeFromCart(@PathVariable long cartItemId, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findUserByUsername(principal.getName());
            cartItemServices.removeFromCart(cartItemId, user);
            redirectAttributes.addFlashAttribute("message", "Item removed from cart");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cart";
        }return "redirect:/cart";
    }
}
