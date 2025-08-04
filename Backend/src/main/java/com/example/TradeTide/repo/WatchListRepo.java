package com.example.TradeTide.repo;

import com.example.TradeTide.model.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchListRepo extends JpaRepository<Watchlist, Long> {
    Watchlist findByUserId(int userId);
}
