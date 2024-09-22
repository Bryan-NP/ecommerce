package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.models.Category;
import com.example.demo.models.User;

public interface CategoryRepo extends JpaRepository<Category,Long> {
    
}   

