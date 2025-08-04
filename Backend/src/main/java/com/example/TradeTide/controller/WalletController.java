package com.example.TradeTide.controller;

import com.example.TradeTide.model.Order;
import com.example.TradeTide.model.User;
import com.example.TradeTide.model.Wallet;
import com.example.TradeTide.model.WalletTransaction;
import com.example.TradeTide.service.OrderService;
import com.example.TradeTide.service.UserService;
import com.example.TradeTide.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class WalletController {
    @Autowired
    private WalletService walletService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @GetMapping("/api/wallet")
    public ResponseEntity<Wallet> getUserWallet(Authentication authentication) throws Exception {
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);
        Wallet wallet = walletService.getUserWallet(user);
        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    @PutMapping("/api/wallet/{walletId}/transfer")
    public ResponseEntity<Wallet> walletToWalletTransfer(
            Authentication authentication,
            @PathVariable Long walletId,
            @RequestBody WalletTransaction req) throws Exception {
        String email = authentication.getName();
        User senderUser = userService.findUserByEmail(email);
        Wallet receiverWallet = walletService.findWalletById(walletId);
        Wallet wallet = walletService.walletToWalletTransfer(senderUser, receiverWallet, req.getAmount());
        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }
    @PutMapping("/api/wallet/order/{orderId}/pay")
    public ResponseEntity<Wallet> payOrderPayment(
            Authentication authentication,
            @PathVariable Long orderId) throws Exception {
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);
        Order order = orderService.getOrderById(orderId);
        Wallet wallet = walletService.payOrderPayment(order, user);
        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }
}
