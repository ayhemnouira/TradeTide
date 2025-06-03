package com.example.TradeTide.controller;

import com.example.TradeTide.model.User;
import com.example.TradeTide.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    private AuthService authService;
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return authService.register(user); // Return the user object as a response
    }
    @PostMapping("/login")
    public String login(@RequestBody User user) {

        return authService.verify(user);
    }
}
