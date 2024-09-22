package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;

import com.example.demo.models.OrderItem;
import com.example.demo.models.Orders;
import com.example.demo.models.Product;
import com.example.demo.models.User;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.example.demo.repo.OrderItemRepo;
import com.stripe.model.LineItem;
import com.example.demo.repo.OrderRepo;
import com.example.demo.repo.ProductRepo;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionListLineItemsParams;


import com.example.demo.repo.UserRepo;

@Service
public class OrderService {
    private final OrderRepo orderRepo;
    private final UserRepo UserRepo;
    private final ProductRepo productRepo;
    private final OrderItemRepo orderItemRepo;


    @Autowired
    public OrderService(OrderRepo orderRepo, UserRepo UserRepo, ProductRepo productRepo, OrderItemRepo orderItemRepo) {
        this.orderRepo = orderRepo;
        this.UserRepo = UserRepo;
        this.productRepo = productRepo;
        this.orderItemRepo = orderItemRepo;
    }

    // Method to retrieve all orders
    public List<Orders> getAllOrders() {
        return orderRepo.findAll(); 
    }

    private User getUserFromPrincipal(Principal principal) {
        // If not logged in, return null
        if(principal == null) {
            return null;
        }
        // Return the user object if logged in
        return UserRepo.findByUsername(principal.getName());
    }

    //Orders from a user
    public List<Orders> findByUser(Principal principal) {
        //We need the current user, so principal
        //Call the method above to retrieve the order by the user
        User user = getUserFromPrincipal(principal);
        return orderRepo.findByUser(user);
    }
    
    // Method to retrieve a particular order
    public List<OrderItem> getOrder(Long id) {
        return orderItemRepo.findByOrder(id); 
    }
    public void handleSuccessfulPayment(Event event) {
        System.out.println("STRIPE API VERSION =" + Stripe.API_VERSION);
        // Get the session
        Session session = (Session) event.getDataObjectDeserializer().getObject().get();
        // String sessionId = session.getId();
        long userId = Long.valueOf(session.getClientReferenceId());
        System.out.println("User ID:" + userId);
        try {
            // expand each line item
            SessionListLineItemsParams listItemParams = SessionListLineItemsParams.builder()
                    .addExpand("data.price.product")
                    .build();
            // Get list
            List<LineItem> lineItems = session.listLineItems(listItemParams).getData();
            // Store a dictionary of product id and quantity
            Map<String, Long> orderedProducts = new HashMap<>();
            for (LineItem item : lineItems) {
                String productId = item.getPrice().getProductObject().getMetadata().get("product_id");
                if (productId == null || productId.isEmpty()) {
                    System.out.println("Unable to get product id for line item " + item);
                    continue;
                }
                long quantity = item.getQuantity();
                orderedProducts.put(productId, quantity);

            }
            
            //Save the user order information to the database
            Orders order = new Orders();
            //New order, by default status = Processing
            order.setStatus("Processing");
            //Set the order to contain the userId
            order.setUser(UserRepo.findById(userId).get());
            //Save the order to the database
            orderRepo.save(order);

            for (Map.Entry<String,Long> entry : orderedProducts.entrySet()) {
                //Retrieve product, quantity and price.
                String productId = entry.getKey();
                long quantity = entry.getValue();
                //Retrieve the product from the database
                Product product = productRepo.findById(Long.valueOf(productId)).get();
                //Store it into orderItem table via orderItemRepo
                OrderItem orderItem = new OrderItem(order,product,quantity,product.getPrice().doubleValue());
                orderItemRepo.save(orderItem);
            }

            
        } catch (StripeException e) {
            System.out.println(e);
        }
    }
}
