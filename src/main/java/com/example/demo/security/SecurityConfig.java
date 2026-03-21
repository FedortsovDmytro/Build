//package com.example.demo.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http
//                .csrf(csrf -> csrf.disable())
//
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/register", "/login", "/verify", "/auth/**", "/css/**").permitAll()
//
//                        .requestMatchers("/employee/**").hasRole("EMPLOYEE")
//
//
//                        .requestMatchers("/manager/**").hasRole("MANAGER")
//
//
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/home").authenticated()
//
//                        .anyRequest().authenticated()
//                )
//                .formLogin(login -> login
//                        .loginPage("/login")
//                        .successHandler(new CustomSuccessHandler())
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/login")
//                );
//
//        return http.build();
//    }
//}
package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Disable CSRF for simplicity (adjust if using forms/REST API)
                .csrf(csrf -> csrf.disable())

                // Configure access rules
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/register", "/login", "/verify", "/auth/**", "/css/**").permitAll()

                        // Employee endpoints
                        .requestMatchers("/employee/**").hasRole("EMPLOYEE")

                        // Manager endpoints
                        .requestMatchers("/manager/**").hasRole("MANAGER")

                        // Admin endpoints
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Home page accessible by any authenticated user
                        .requestMatchers("/home").authenticated()

                        // All other requests require authentication
                        .anyRequest().authenticated()
                )

                // Login configuration
                .formLogin(login -> login
                        .loginPage("/login")           // Custom login page
                        .permitAll()                   // Allow everyone to access login
                        .successHandler(new CustomSuccessHandler()) // Redirect after login based on role
                )

                // Logout configuration
                .logout(logout -> logout
                        .logoutUrl("/logout")          // Logout URL
                        .logoutSuccessUrl("/login")    // Redirect to login after logout
                        .invalidateHttpSession(true)   // Invalidate session
                        .deleteCookies("JSESSIONID")   // Clear cookies
                        .permitAll()
                );

        return http.build();
    }
}