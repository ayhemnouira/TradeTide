package com.example.TradeTide.service;

import com.example.TradeTide.model.Asset;
import com.example.TradeTide.model.Coin;
import com.example.TradeTide.model.User;

import java.util.List;

public interface AssetService {
    Asset createAsset(User user, Coin coin, double quantity);
    Asset getAssetById(Long assetId) throws Exception;
    Asset getAssetByUserIdAndId(int userId, Long assetId);
    List<Asset> getUsersAssets(int userId);
    Asset updateAsset(Long assetId, double quantity) throws Exception;
    Asset findAssetByUserIdAndCoinId(int userId, String coinId);
    void deleteAsset(Long assetId);
}
