package com.example.TradeTide.service;

import com.example.TradeTide.domain.OrderStatus;
import com.example.TradeTide.domain.OrderType;
import com.example.TradeTide.model.*;
import com.example.TradeTide.repo.OrderItemRepo;
import com.example.TradeTide.repo.OrderRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private WalletService walletService;
    @Autowired
    private OrderItemRepo orderItemRepo;
    @Autowired
    private AssetService assetService;
    @Override
    public Order createOrder(User user, OrderItem orderItem, OrderType orderType) throws Exception {
        double price = orderItem.getCoin().getCurrentPrice()* orderItem.getQuantity();
        Order order = new Order();
        order.setUser(user);
        order.setOrderItem(orderItem);
        order.setOrderType(orderType);
        order.setPrice(BigDecimal.valueOf(price));
        order.setTimestamp(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        return orderRepo.save(order);
    }

    @Override
    public Order getOrderById(Long orderId) throws Exception {
        return orderRepo.findById(orderId).orElseThrow(() -> new Exception("Order not found with ID: " + orderId));
    }

    @Override
    public List<Order> getAllOrdersOfUser(int userId, OrderType orderType, String assetSymbol) throws Exception {
        return orderRepo.findByUserId(userId);
    }

    private OrderItem createOrderItem(Coin coin, double quantity,double buyPrice, double sellPrice) {
        OrderItem orderItem = new OrderItem();
        orderItem.setCoin(coin);
        orderItem.setQuantity(quantity);
        orderItem.setBuyPrice(buyPrice);
        orderItem.setSellPrice(sellPrice);
        return orderItemRepo.save(orderItem);
    }
    @Transactional
    public Order buyAsset(Coin coin, double quantity, User user) throws Exception {
       if (quantity <= 0) {
            throw new Exception("Quantity must be greater than zero");
        }
       double buyPrice = coin.getCurrentPrice();
        OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, 0);
        Order order = createOrder(user, orderItem, OrderType.BUY);
        orderItem.setOrder(order);
        walletService.payOrderPayment(order,user);
        order.setStatus(OrderStatus.SUCCESS);
        order.setOrderType(OrderType.BUY);
        Order savedOrder = orderRepo.save(order);

        Asset oldAsset = assetService.findAssetByUserIdAndCoinId(order.getUser().getId(),
                order.getOrderItem().getCoin().getId());
        if( oldAsset == null) {
          assetService.createAsset(user, orderItem.getCoin(), orderItem.getQuantity());

        } else {
           assetService.updateAsset(oldAsset.getId(), quantity);
            ;
        }
        return savedOrder;
    }
    @Transactional
    public Order sellAsset(Coin coin, double quantity, User user) throws Exception {
        if (quantity <= 0) {
            throw new Exception("Quantity must be greater than zero");
        }
        double sellPrice = coin.getCurrentPrice();
        Asset assetToSell = assetService.findAssetByUserIdAndCoinId(user.getId(), coin.getId());
        if (assetToSell == null) {
            throw new Exception("Asset not found for user");
        }
        double buyPrice = assetToSell.getBuyPrice();
        OrderItem orderItem = createOrderItem(coin, quantity, 0, sellPrice);
        Order order = createOrder(user, orderItem, OrderType.SELL);
        orderItem.setOrder(order);

        if (assetToSell.getQuantity() >= quantity) {
            order.setStatus(OrderStatus.SUCCESS);
            order.setOrderType(OrderType.SELL);
            Order savedOrder = orderRepo.save(order);
            walletService.payOrderPayment(order, user);
            Asset updatedAsset = assetService.updateAsset(assetToSell.getId(), -quantity);
            if (updatedAsset.getQuantity() * coin.getCurrentPrice() <= 1) {
                assetService.deleteAsset(updatedAsset.getId());
            }
            return savedOrder;
        }
        throw new Exception("Insufficient asset quantity to sell");
    }
    @Override
    @Transactional
    public Order ProcessOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception {
        if(orderType == OrderType.BUY) {
            return buyAsset(coin, quantity, user);
        } else if(orderType == OrderType.SELL) {
            return sellAsset(coin, quantity, user);
        }
        throw  new Exception("Invalid order type");
    }
}
