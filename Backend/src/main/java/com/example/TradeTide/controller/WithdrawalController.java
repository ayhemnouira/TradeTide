package com.example.TradeTide.controller;

import com.example.TradeTide.model.User;
import com.example.TradeTide.model.Wallet;
import com.example.TradeTide.model.Withdrawal;
import com.example.TradeTide.service.UserService;
import com.example.TradeTide.service.WalletService;
import com.example.TradeTide.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WithdrawalController {
    @Autowired
    private WithdrawalService withdrawalService;
    @Autowired
    private UserService userService;
    @Autowired
    private WalletService walletService;
    @PostMapping("/api/withdrawal/{amount}")
    public ResponseEntity<?> withdrawalRequest(@PathVariable Long amount, Authentication authentication ) throws Exception {
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);
        Wallet userWallet = walletService.getUserWallet(user);
        Withdrawal withdrawal = withdrawalService.requestWithdrawal(amount, user);
        walletService.addBalance(userWallet, -withdrawal.getAmount());
        return new ResponseEntity<>(withdrawal, HttpStatus.ACCEPTED);
    }
    @PatchMapping("/api/admin/withdrawal/{id}/proceed/{accept}")
    public ResponseEntity<?> proceedWithdrawal(@PathVariable Long id, @PathVariable boolean accept,Authentication authentication) throws Exception {
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);
        Withdrawal withdrawal = withdrawalService.procedWithwithdrawal(id, accept);
        Wallet userWallet = walletService.getUserWallet(user);
        if (!accept) {
            walletService.addBalance(userWallet, withdrawal.getAmount());
        }
        return new ResponseEntity<>(withdrawal, HttpStatus.OK);
    }
    @GetMapping("/api/withdrawal")
    public ResponseEntity<List<Withdrawal>> getUserWithdrawalHistory(Authentication authentication) throws Exception{
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);
        List<Withdrawal> withdrawalList = withdrawalService.getUsersWithdrawalHistory(user);
        return new ResponseEntity<>(withdrawalList, HttpStatus.OK);
    }
    @GetMapping("/api/admin/withdrawal")
    public ResponseEntity<List<Withdrawal>> getAllWithdrawalRequest(Authentication authentication) {
        List<Withdrawal> withdrawalList = withdrawalService.getAllWithdrawalRequest();
        return new ResponseEntity<>(withdrawalList, HttpStatus.OK);
    }
}
