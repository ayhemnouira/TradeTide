package com.example.TradeTide.controller;

import com.example.TradeTide.model.User;
import com.example.TradeTide.response.PaymentResponse;
import com.example.TradeTide.service.Stripe.StripeService;
import com.example.TradeTide.service.UserService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/*
@RestController
@RequestMapping("/api")
public class PaymentController {
    @Autowired
    private StripeService stripeService;
    @Autowired
    private UserService userService;
    @PostMapping("/api/payment/amount/{amount}")
    public ResponseEntity<PaymentResponse> paymentHandler(@PathVariable Long amount
    , Authentication authentication) throws Exception, StripeException {
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);

        return ResponseEntity.ok(paymentResponse);
    }
}

 */
