package com.example.demo.models;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.*;
import java.util.*;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "Name cannot be blank")
    @Size(min=2,max = 100, message="Name must be between 2 and 100 characters")
    @Column(nullable = false)
    private String name;

    @DecimalMin(value = "0.01", message = "Price must be greater than 0.01")
    @Column(nullable = false, precision = 10,scale = 2)
    private BigDecimal price;

    //Create the foreign key for the product
    @ManyToOne(fetch = FetchType.LAZY,optional=false)
    // the name will be the column name in the table
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

    //Use a set, can contain many, can reject duplicates
    //mappedBy is always in the context of other side of the relationship
    @ManyToMany(cascade ={CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(name="products_tags",
            joinColumns = {@JoinColumn(name="product_id")},
            inverseJoinColumns = {@JoinColumn(name="tag_id")})
    private Set<Tag> tags = new HashSet<>();


    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.getProducts();
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getProducts().remove(this);
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Product() {

    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Product(String description, String name, BigDecimal price) {
        this.description = description;
        this.name = name;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    // toString method
   @Override
   public String toString() {
       return "Product{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", price=" + price +
               '}';
   }

   // equals and hashCode methods
   @Override
   public boolean equals(Object o) {
       if (this == o) return true;
       if (o == null || getClass() != o.getClass()) return false;
       Product product = (Product) o;
       return Objects.equals(id, product.id) &&
               Objects.equals(name, product.name) &&
               Objects.equals(description, product.description) &&
               Objects.equals(price, product.price);
   }

   @Override
   public int hashCode() {
       return Objects.hash(id, name, description, price);
   }
}


