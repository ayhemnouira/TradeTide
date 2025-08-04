package com.example.TradeTide.service;

import com.example.TradeTide.model.Coin;
import com.example.TradeTide.model.User;
import com.example.TradeTide.model.Watchlist;
import com.example.TradeTide.repo.WatchListRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WatchListServiceImpl implements WatchListService {
    @Autowired
    private WatchListRepo watchListRepository;

    @Override
    public Watchlist findUserWatchList(int userId) throws Exception {
        Watchlist watchlist = watchListRepository.findByUserId(userId);
        if (watchlist == null) {
            throw new Exception("Watchlist not found");
        }
        return watchlist;
    }

    @Override
    public Watchlist createWatchList(User user) {
        Watchlist watchlist = new Watchlist();
        watchlist.setUser(user);
        return watchListRepository.save(watchlist);
    }

    @Override
    public Watchlist findById(Long id) throws Exception {
        Optional<Watchlist> watchlistOptional = watchListRepository.findById(id);
        if (watchlistOptional.isEmpty()) {
            throw new Exception("Watchlist not found");
        }
        return watchlistOptional.get();
    }

    @Override
    public Coin addItemToWatchList(Coin coin, User user) throws Exception {
        Watchlist watchlist = findUserWatchList(user.getId());
        if(watchlist.getCoins().contains(coin)) {
            watchlist.getCoins().remove(coin);
        }
        else watchlist.getCoins().add(coin);
        watchListRepository.save(watchlist);
        return  coin;
    }
}
