package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.services.OrderService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;

// a @RestController by default will return a JSON response
// a @controller by default will render a template

@RestController
@RequestMapping("/stripe/webhook")
public class StripeWebHookController {
    @Value("${stripe.webhook.secret}")
    private String endpointSecret;
    private final OrderService orderService;
    @Autowired
    public StripeWebHookController(OrderService orderService){
        this.orderService = orderService;
    }

    //Stripe-Signature: whenever Stripe calls our endpoint (aka route),
    // it will enclose a signature for security purposes
    @PostMapping
    public ResponseEntity<String> handleStripeEvent(@RequestBody String payload, 
    @RequestHeader("Stripe-Signature") String sigHeader){
       //Extract the event from the request
       Event event = null;
       System.out.println("Stripe webhook called");
       try{
        //Construct the event
        event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
       }catch(SignatureVerificationException e){
        System.out.println(e);
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
       }
       System.out.println(event);
       if(event.getType().equals("checkout.session.completed")){
           //handle processing of order
           orderService.handleSuccessfulPayment(event);
       }
       return ResponseEntity.ok().build(); // send back http status code 200

}
}
