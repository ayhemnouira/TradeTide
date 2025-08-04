package com.example.TradeTide.repo;

import com.example.TradeTide.model.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinRepo extends JpaRepository<Coin, String> {
}
