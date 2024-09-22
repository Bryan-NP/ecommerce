package com.example.demo.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

//A controller advice is applied to all other controllers
// you can put filtering rules to state for a particular advice, which URL it applies to
@ControllerAdvice
public class GlobalControllerAdvice {
    @ModelAttribute("currentUser")
    public String getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()){
            return null;
        }return authentication.getName();
    }
    //This advice is applied whenever the templates refer to the currentUser var
    @ModelAttribute("isAuthenticated")
    public boolean isAuthenticated(){
        //get the current authentication context(i.e the current details of the logged in user)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //if no user is logged in, return null
        if(authentication.getName().equals("anonymousUser")){
            return false;
        }
        //else return the name of the current logged in user
        return true;
        
    }
   
}
