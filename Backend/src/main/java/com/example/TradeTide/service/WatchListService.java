package com.example.TradeTide.service;

import com.example.TradeTide.model.Coin;
import com.example.TradeTide.model.User;
import com.example.TradeTide.model.Watchlist;

public interface WatchListService {
    Watchlist findUserWatchList(int userId) throws Exception;
    Watchlist createWatchList(User user);
    Watchlist findById(Long id) throws Exception;
    Coin addItemToWatchList(Coin coin, User user) throws Exception;
}
