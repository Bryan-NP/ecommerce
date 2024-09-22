package com.example.demo.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import com.example.demo.models.CartItem;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData.ProductData;

import jakarta.annotation.PostConstruct;

@Service
public class StripeService {
    @Value("${stripe.api.secretKey}")
    private String stripeSecretKey;
    @Value("${stripe.api.publicKey}")
    private String stripePublicKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    public Session createCheckoutSession(List<CartItem> cartItems, long userId, String successUrl, String cancelUrl)
            throws StripeException {
        // 1. Create line items
        // a line item is the description of the product + quantity + price per unit
        // (i.e one line in a invoice)
        // 2. pass all the line items, along with apyment requirements (like what
        // currency) to Stripe
        // 3. receive the checkout session id from Stripe

        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();
        for (CartItem item : cartItems) {
            // Line item consist of product, price, quantity.
            // This is only for product.
            var productData = SessionCreateParams.LineItem.PriceData.ProductData.builder()
                    .setName(item.getProduct().getName())
                    .putMetadata("product_id", Long.toString(item.getProduct().getId()))
                    .build();
            // Price consists of the unit amount, and the currency and the product
            // information.
            var priceData = SessionCreateParams.LineItem.PriceData.builder()
                    .setCurrency("usd")
                    .setUnitAmount(item.getProduct().getPrice().multiply(new BigDecimal(100)).longValue())
                    .setProductData((ProductData) productData)
                    .build();

            // Line items consists of quantity and the price data
            var lineItem = SessionCreateParams.LineItem.builder()
                    .setPriceData(priceData)
                    .setQuantity((long) item.getQuantity())
                    .build();

            lineItems.add(lineItem);
        }

        // Create the checkout session
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCancelUrl(cancelUrl)
                .setSuccessUrl(successUrl)
                .addAllLineItem(lineItems)
                .setClientReferenceId(Long.toString(userId))
                .build();
        return Session.create(params);
    }

    public String getPublicKey() {
        return stripePublicKey;
    }
}
