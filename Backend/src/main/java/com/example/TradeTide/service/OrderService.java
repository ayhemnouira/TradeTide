package com.example.TradeTide.service;

import com.example.TradeTide.domain.OrderType;
import com.example.TradeTide.model.Coin;
import com.example.TradeTide.model.Order;
import com.example.TradeTide.model.OrderItem;
import com.example.TradeTide.model.User;

import java.util.List;

public interface OrderService {
    Order createOrder(User user, OrderItem orderItem, OrderType orderType) throws Exception;
    Order getOrderById(Long orderId) throws Exception;
    List<Order> getAllOrdersOfUser(int userId,OrderType orderType, String assetSymbol) throws Exception;
    Order ProcessOrder(Coin coin, double quantity , OrderType orderType , User user) throws Exception;
}
