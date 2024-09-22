package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

// @Configuration is a Spring annotation that indicates that the class is a configuration class
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // @Bean is a Spring annotation that indicates that a method produces a bean to be managed by the Spring container
    // Think of it as a "factory method" that spring boot will use later, sometimes internally.
    // To create an instance of a component that it needs
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //http security calss allow us to define security rules
        http
        .csrf(csrf -> csrf.ignoringRequestMatchers("/stripe/webhook"))
        .authorizeHttpRequests(authz -> 
            // All of the URLS can be accessed without login
            authz.requestMatchers("/register","/login","/css/**","/js/**","/stripe/webhook").permitAll()
            .anyRequest().authenticated()
        )
        //Allow all users to login
        .formLogin(formLogin -> formLogin
            .loginPage("/login")
            .permitAll()
        ).logout(logout -> logout.permitAll());
        return http.build();
    }
    
}
