package com.example.TradeTide.repo;

import com.example.TradeTide.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepo extends JpaRepository<Asset, Long> {
    List<Asset> findByUserId(int userId);
    Asset findByUserIdAndCoinId(int userId, String coinId);
}
