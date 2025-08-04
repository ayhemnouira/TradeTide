package com.example.TradeTide.repo;

import com.example.TradeTide.model.PaymentDetails;
import com.example.TradeTide.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentDetailsRepo extends JpaRepository<PaymentDetails, Long> {
    PaymentDetails findByUserId(int userId) ;
}
