package com.example.TradeTide.controller;

import com.example.TradeTide.model.PaymentDetails;
import com.example.TradeTide.model.User;
import com.example.TradeTide.service.PaymentDetailsService;
import com.example.TradeTide.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentDetailsController {
    @Autowired
    private PaymentDetailsService paymentDetailsService;
    @Autowired
    private UserService userService;

    @PostMapping("/payment-details")
    public ResponseEntity<PaymentDetails> addPaymentDetails(@RequestBody PaymentDetails paymentDetailsRequest,
                                            Authentication authentication) throws Exception {
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);
        PaymentDetails paymentDetails =  paymentDetailsService.addPaymentDetails(paymentDetailsRequest.getAccountNumber(),
                paymentDetailsRequest.getAccountHolderName(),
                paymentDetailsRequest.getIfscCode(),
                paymentDetailsRequest.getBankName(), user);
        return new  ResponseEntity<>(paymentDetails, HttpStatus.CREATED);
    }

    @GetMapping("/payment-details")
    public ResponseEntity<PaymentDetails> getPaymentDetails(Authentication authentication) throws Exception {
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);
        PaymentDetails paymentDetails = paymentDetailsService.getPaymentDetailsByUser(user);
        return new ResponseEntity<>(paymentDetails, HttpStatus.OK);
    }
}
