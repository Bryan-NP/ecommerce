package com.example.demo.controllers;

import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.models.Product;
import com.example.demo.repo.CategoryRepo;
import com.example.demo.repo.ProductRepo;
import com.example.demo.repo.TagRepo;
import com.example.demo.models.Tag;

import jakarta.validation.Valid;

@Controller
public class ProductController {
    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final TagRepo tagRepo;

    @Autowired
    // Dependency Injection = When Spring Boot creates an instance
    // of ProductController, it will automatically create an instance of ProductRepo
    // and pass it to the new instance of ProductController
    public ProductController(ProductRepo productRepo,CategoryRepo categoryRepo, TagRepo tagRepo){ 
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.tagRepo = tagRepo;
    }
    @GetMapping("/products")
    public String listProducts(Model model){
        List<Product> products = productRepo.findAllWithCategoriesAndTags();
        model.addAttribute("products", products);
        return "products/index";
    }

    //When we want to add forms, we always need 2 routes
    //1 to display 1 to process
    @GetMapping("/products/create")
    public String showCreateProductForm(Model model){
        //Send an empty instance of the product model to the template
        var newProduct = new Product();
        //find all the categories and add it to the view model
        model.addAttribute("categories", categoryRepo.findAll());
        //add the instance of the new product model to the new model
        model.addAttribute("product", newProduct);
        //get all the tags and add it to the view model
        model.addAttribute("allTags", tagRepo.findAll());
        return "products/create";
    }
    // The results of the validation will be in the bindingResult parameter
    @PostMapping("/products/create")
    public String processCreateProductForm(@Valid @ModelAttribute Product newProduct,
    BindingResult bindingResult,Model model,@RequestParam(required=false) List<Long> tagIds){
        
        if(bindingResult.hasErrors()){
            //If there are errors, return the form again
            //and skip the rest of the method
            model.addAttribute("categories", categoryRepo.findAll());
            model.addAttribute("allTags", tagRepo.findAll());
            return "products/create";
        }

        //Check if the user has selected any tags
        if(tagIds !=null){
            var tags = new HashSet<Tag>(tagRepo.findAllById(tagIds));
            newProduct.setTags(tags);
        }

        //Save the product to the database
        productRepo.save(newProduct);
        //Redirect to the list of products
        return "redirect:/products";

    }

    // This is known as a URL parameter
    @GetMapping("/products/{id}")
    public String productDetails(@PathVariable Long id, Model model){
        //Find the product by the id
        var product = productRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        //Add the product to the model
        model.addAttribute("product", product);
        // details.html in the products folder
        return "products/details";
        
    }

    // 1. find by ID the product thaat the user wants to update
    // 2. display the form which contains the existing data of the product
    @GetMapping("/products/{id}/edit")
    public String showUpdateProduct(@PathVariable Long id, Model model){
        //Find the product by the id
        var product = productRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        
        //find all the categories and add it to the view model


        //Add the product to the model
        model.addAttribute("product", product);
        //This returns the file edit.html in the products folder
        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("allTags", tagRepo.findAll());
        return "products/edit";
    }

    @PostMapping("/products/{id}/edit")
    public String updateProduct(@Valid @PathVariable Long id, @ModelAttribute Product product,
    BindingResult bindingResult,Model model,@RequestParam(required=false) List<Long> tagIds){
        product.setId(id);
        
        if(bindingResult.hasErrors()){
            model.addAttribute("product", product);
            model.addAttribute("categories",categoryRepo.findAll());
            model.addAttribute("allTags", tagRepo.findAll());
            return "redirect:/products/" + id +"/edit";
        }
        
        //update the tags on the product
        if(tagIds !=null && !tagIds.isEmpty()){
            var tags = new HashSet<Tag>(tagRepo.findAllById(tagIds));
            product.setTags(tags);
        }
        productRepo.save(product);
        //This redirects to the products webpage
        return "redirect:/products";
    }

    //we have 2 routes for deleting
    //1 showing a delete form( asking the user if they are sure)
    @GetMapping("/products/{id}/delete")
    public String showDeleteProductForm(@PathVariable Long id, Model model){
        // find the product that we really want to delete
        var product = productRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        // add the product to the model
        model.addAttribute("product", product);
        return "products/delete";
    }
    //2 delete the process
    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id){
        //delete the product by id
        productRepo.deleteById(id);
        //redirect to the products page
        return "redirect:/products";
    }
}

