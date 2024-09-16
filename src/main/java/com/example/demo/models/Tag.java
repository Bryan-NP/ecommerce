package com.example.demo.models;

import jakarta.persistence.*;
import java.util.*;
@Entity
@Table(name = "tags")
public class Tag {
    //Primary key, set to auto-increment
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable =false, unique = true)
    private String name;

    //mappedbBy is always in the context of other side of the relationship
    //meaning, the product model is supposed to have a field called tags
    @ManyToMany(mappedBy = "tags")
    private Set<Product> products = new HashSet<>();


    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
    
    // Helper methods to faciliate the adding and removing of products
    // from a tag
    public void addProduct(Product product) {
        this.products.add(product);
        product.getTags().add(this);
    }

    // Helper method to remove a product
    public void removeProduct(Product product) {
        this.products.remove(product);
        product.getTags().remove(this);
    }

    // toString method
    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    // equals and hashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id) &&
                Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    
}
