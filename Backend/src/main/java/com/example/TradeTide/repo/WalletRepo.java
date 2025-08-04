package com.example.TradeTide.repo;

import com.example.TradeTide.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepo extends JpaRepository<Wallet, Long> {
    Wallet findByUserId(int userId);
}
