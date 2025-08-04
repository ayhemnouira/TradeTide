package com.example.TradeTide.repo;

import com.example.TradeTide.model.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOrderRepo extends JpaRepository<PaymentOrder, Long> {
    PaymentOrder findByUserId(int userId);
}
