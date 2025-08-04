package com.example.TradeTide.controller;

import com.example.TradeTide.domain.OrderType;
import com.example.TradeTide.model.Coin;
import com.example.TradeTide.model.Order;
import com.example.TradeTide.model.User;
import com.example.TradeTide.request.CreateOrderRequest;
import com.example.TradeTide.service.CoinService;
import com.example.TradeTide.service.OrderService;
import com.example.TradeTide.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private CoinService coinService;
  /*  @Autowired
    private WalletTransactionService walletTransactionService;*/

    @PostMapping("/pay")
    public ResponseEntity<Order> payOrderPayment(Authentication authentication,
                                                 @RequestBody CreateOrderRequest req
                                                 )throws Exception{
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);
        Coin coin = coinService.findById(req.getCoinId());
        Order order = orderService.ProcessOrder(coin, req.getQuantity(), req.getOrderType(), user);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(Authentication authentication
            ,@PathVariable Long orderId) throws Exception {
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);
        Order order = orderService.getOrderById(orderId);
        if (order.getUser().getId() == user.getId()) {
            return ResponseEntity.ok(order);
        }else {
         throw new Exception("you don't have permission to view this order");
        }
    }
    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrdersForUser(Authentication authentication,
                                                           @RequestParam (required = false) OrderType order_type,
                                                           @RequestParam(required = false) String asset_symbol) throws Exception {
        String email = authentication.getName();
        int  userId = userService.findUserByEmail(email).getId();
        List<Order> orders = orderService.getAllOrdersOfUser(userId, order_type, asset_symbol);
        return ResponseEntity.ok(orders);
    }
}
