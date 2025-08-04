package com.example.TradeTide.repo;

import com.example.TradeTide.model.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WithdrawalRepo extends JpaRepository<Withdrawal, Long> {
    List<Withdrawal> findByUserId(int userId);
}
