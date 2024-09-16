package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.models.User;

public interface UserRepo extends JpaRepository<User,Long> {
    //Automated query generation
    User findByUsername(String username);
    
}
