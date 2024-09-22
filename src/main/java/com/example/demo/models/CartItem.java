package com.example.demo.models;
import java.math.BigDecimal;
import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   
    // one user can own many cart items
    // a cart item is only owned by 1 user
    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name="product_id", nullable = false)
    private Product product;
    private int quantity;

    public CartItem() {
    }

    public CartItem(User user, Product product,int quantity) {
        this.quantity = quantity;
        this.user = user;
        this.product = product;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    
    public BigDecimal getTotalPrice(){
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

}
