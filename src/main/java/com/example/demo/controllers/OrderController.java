package com.example.demo.controllers;

import java.security.Principal;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.models.Orders;
import com.example.demo.models.User;
import com.example.demo.services.OrderService;
import com.example.demo.services.UserService;
import com.example.demo.repo.*;

@Controller
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final UserRepo userRepo;
    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;

    @Autowired
    public OrderController(OrderService orderService,OrderItemRepo orderItemRepo, UserService userService, OrderRepo orderRepo,UserRepo userRepo) {
        this.orderService = orderService;
        this.userRepo = userRepo;
        this.orderRepo = orderRepo;
        this.userService = userService;
        this.orderItemRepo = orderItemRepo;
    }

    // Show all orders by user
    @GetMapping("")
    public String getAllOrders(Model model, Principal principal) {
        //Get all orders
        List<Orders> orders = orderService.getAllOrders();
        //Portion to add in
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        String email = user.getEmail();
        //Added portion
        model.addAttribute("email",email);
        //End
        model.addAttribute("order", orders);
        return "order/index";
    }

    // Get a particular order
    @GetMapping("/{id}")
    public String getOrder(@PathVariable Long id, Model model, Principal principal) {
        var order = orderService.getOrder(id);
        String username = principal.getName();
        var orderItem = orderItemRepo.findByOrder(id);

        model.addAttribute("orderItem",orderItem);
        model.addAttribute("username",username);
        model.addAttribute("order", order);
        return "order/show";
    }

    //Delete form
    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id,Model model,Principal principal,RedirectAttributes redirectAttributes) {
        var order = orderRepo.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + id));
        model.addAttribute("deleteOrder", order);
        return "order/delete";   
    }

    //Process Delete
    @PostMapping("/delete/{id}")
    public String processDeleteOrder(@PathVariable Long id,RedirectAttributes redirectAttributes) {
         // Find the order
        var order = orderRepo.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + id));

        // Delete associated order items first
        orderItemRepo.deleteByOrderId(order.getId());
        orderRepo.deleteById(id);
        return "redirect:/order";
    }

    // Get edit form for a particular order item
@GetMapping("/edit/{id}")
public String getEditOrderItem(@PathVariable Long id, Model model) {
    var orderItem = orderRepo.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Invalid item Id:" + id));
    model.addAttribute("orderItem", orderItem);
    return "order/edit";
}

// Process the edited order item
@PostMapping("/edit/{id}")
public String processEditOrderItem(@PathVariable Long id, 
                                    @RequestParam String status, 
                                    RedirectAttributes redirectAttributes) {
    var orderItem = orderRepo.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Invalid item Id:" + id));
    System.out.println("hi" + status);
    orderItem.setStatus(status);
    orderRepo.save(orderItem);
    
    redirectAttributes.addFlashAttribute("message", "Order item updated successfully!");
    return "redirect:/order"; // Redirect back to the order list
}

}
