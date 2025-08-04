package com.example.TradeTide.repo;

import com.example.TradeTide.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {
   List<Order> findByUserId(int userId);
}
