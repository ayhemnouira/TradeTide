package com.example.TradeTide.controller;

import com.example.TradeTide.model.Asset;
import com.example.TradeTide.model.User;
import com.example.TradeTide.service.AssetService;
import com.example.TradeTide.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asset")
public class AssetController {
    @Autowired
    private AssetService assetService;
    @Autowired
    private UserService userService;

    @GetMapping("/{assetId}")
    public ResponseEntity<Asset> getAssetById(@PathVariable Long assetId) throws Exception {
        return ResponseEntity.ok().body(assetService.getAssetById(assetId));
    }
    @GetMapping("/coin/{coinId}/user")
    public ResponseEntity<Asset> getAssetByUserIdAndCoinId(@PathVariable String coinId,
                                                           Authentication authentication) throws Exception
    {
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);
        Asset asset = assetService.findAssetByUserIdAndCoinId(user.getId(), coinId);
        return ResponseEntity.ok().body(asset);
    }
    @GetMapping()
    public ResponseEntity<List<Asset>> getAssetsForUser(
            Authentication authentication) throws Exception{
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);
        List<Asset> assets = assetService.getUsersAssets(user.getId());
        return ResponseEntity.ok().body(assets);
    }
}
