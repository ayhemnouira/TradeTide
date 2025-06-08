package com.example.TradeTide.repo;

import com.example.TradeTide.model.TwoFactorOTP;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TwoFactorOtpRepo extends JpaRepository<TwoFactorOTP,String> {
    TwoFactorOTP findByUserId(int userId);
}
