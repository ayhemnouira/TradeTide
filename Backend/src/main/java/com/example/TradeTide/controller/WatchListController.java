package com.example.TradeTide.controller;

import com.example.TradeTide.model.Coin;
import com.example.TradeTide.model.User;
import com.example.TradeTide.model.Watchlist;
import com.example.TradeTide.service.CoinService;
import com.example.TradeTide.service.UserService;
import com.example.TradeTide.service.WatchListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/watchlist")
public class WatchListController {
    @Autowired
    private WatchListService watchListService;
    @Autowired
    private UserService userService;
    @Autowired
    private CoinService coinService;
    @GetMapping("/user")
    public ResponseEntity<Watchlist> getUserWatchList(Authentication authentication) throws Exception {
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);
        Watchlist watchlist = watchListService.findUserWatchList(user.getId());
        return ResponseEntity.ok(watchlist);
    }
    @GetMapping("/{watchlistId}")
    public ResponseEntity<Watchlist> getWatchListById(@PathVariable Long watchlistId) throws Exception {
        Watchlist watchlist = watchListService.findById(watchlistId);
        return ResponseEntity.ok(watchlist);
    }
    @PatchMapping("/add/coin/{coinId}")
    public ResponseEntity<Coin> addItemToWatchlist(Authentication authentication ,@PathVariable String coinId) throws Exception {
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);
        Coin coin = coinService.findById(coinId);
        Coin addedCoin =  watchListService.addItemToWatchList(coin, user);
        return ResponseEntity.ok(addedCoin);
    }
}
