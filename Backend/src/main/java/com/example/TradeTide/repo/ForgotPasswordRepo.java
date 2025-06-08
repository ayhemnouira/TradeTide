package com.example.TradeTide.repo;

import com.example.TradeTide.model.ForgotPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgotPasswordRepo extends JpaRepository<ForgotPasswordToken, String> {



    ForgotPasswordToken findByUserId(int userId);


}
