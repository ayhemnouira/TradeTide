package com.example.TradeTide.service;

import com.example.TradeTide.model.Order;
import com.example.TradeTide.model.User;
import com.example.TradeTide.model.Wallet;

public interface WalletService {
    Wallet getUserWallet(User user);
    Wallet addBalance(Wallet wallet, Long money);
    Wallet findWalletById(Long id) throws Exception;
    Wallet walletToWalletTransfer(User sender, Wallet receiverWallet, Long amount) throws Exception;
    Wallet payOrderPayment(Order order, User user);
}
