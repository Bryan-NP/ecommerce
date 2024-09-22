package com.example.demo.models;

import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")

public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String status;

    // create the foreign key for the order table
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    // the name will join the order table to the user table
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Orders() {
    }

    public Orders(String status, User user) {
        this.status = status;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // toString method
    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", userId=" + (user != null ? user.getId() : null) +
                '}';
    }

    // equals and hashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Orders orders = (Orders) o;
        return Objects.equals(id, orders.id) &&
                Objects.equals(status, orders.status) &&
                Objects.equals(user, orders.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, user);
    }

}
