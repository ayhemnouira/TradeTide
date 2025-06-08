package com.example.TradeTide.repo;

import com.example.TradeTide.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepo extends JpaRepository<VerificationCode,Long> {
    VerificationCode findByUserId(int userId);
}
