package com.example.demo.controllers;
import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;

// The @Controller annotation tells Spring Boot that is a controller that contains routes
// Spring Boot will automatically go through all the methods marked as routes
// and register them

@Controller
public class HomeController{
    // When the user goes to the / URL on the server, call this method
    @GetMapping("/")
    // Tell Spring Boot this methods returns a HTTP response
    @ResponseBody
    public String helloWorld(){
        return "<h1>Hello World</h1>;";
    }
    // URL-Fragment
    @GetMapping("/about-us")
    // The Model model parameter is automatically passed to aboutUs when is called by Spring.
    // This is called the view model, allow us to inject variables to our template.
    public String aboutUs(Model model){
        //If route is not marked with @responseBody, Spring Boot will look for a template
        //we need to return the file path (without extension) to the template
        //Relative to resource/templates
        LocalDateTime currentDateTime = LocalDateTime.now();
        // w/e the view model is automatically passed to the template and all attirbutes in it will be
        // available in the template
        model.addAttribute("CurrentDateTime", currentDateTime);

        return "about-us";
    }

    @GetMapping("/contact-us")
    public String contactUs(){
        return "contact-us";
    }
       
}