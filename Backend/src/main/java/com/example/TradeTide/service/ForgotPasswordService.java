package com.example.TradeTide.service;

import com.example.TradeTide.domain.VerificationType;
import com.example.TradeTide.model.ForgotPasswordToken;
import com.example.TradeTide.model.User;

public interface ForgotPasswordService {
    ForgotPasswordToken createToken(User user,
                                    String id, String otp,
                                    VerificationType verificationType,
                                    String sendTo);
    ForgotPasswordToken findById(String id);
    ForgotPasswordToken findByUserId(int userId);
    void deleteToken(ForgotPasswordToken token);
}
