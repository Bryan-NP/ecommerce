package com.example.demo.models;

import java.util.Objects;

import jakarta.persistence.*;
@Entity
@Table(name="order_items")

public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    //Create order_id
    @JoinColumn(name="order_id",nullable=false)
    private Orders order;

    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="product_id",nullable=false)
    private Product product;

    @Column(nullable = false)
    //Create quantity
    private long quantity;

    @Column(nullable = false)
    //Create price
    private double price;


    public OrderItem() {
    }

    
    public OrderItem(Orders order, Product product, long quantity, double price) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Orders getOrder() {
        return order;
    }


    public void setOrder(Orders order) {
        this.order = order;
    }


    public Product getProduct() {
        return product;
    }


    public void setProduct(Product product) {
        this.product = product;
    }


    public long getQuantity() {
        return quantity;
    }


    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public double getPrice() {
        return price;
    }


    public void setPrice(double price) {
        this.price = price;
    }

    // toString method
@Override
public String toString() {
    return "OrderItem{" +
            "id=" + id +
            ", orderId=" + (order != null ? order.getId() : null) +
            ", productId=" + (product != null ? product.getId() : null) +
            ", quantity=" + quantity +
            ", price=" + price +
            '}';
}

// equals and hashCode methods
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    OrderItem orderItem = (OrderItem) o;
    return quantity == orderItem.quantity &&
            Double.compare(orderItem.price, price) == 0 &&
            Objects.equals(id, orderItem.id) &&
            Objects.equals(order, orderItem.order) &&
            Objects.equals(product, orderItem.product);
}

@Override
public int hashCode() {
    return Objects.hash(id, order, product, quantity, price);
}

    
}
