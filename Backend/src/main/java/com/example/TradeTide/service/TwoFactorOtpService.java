package com.example.TradeTide.service;

import com.example.TradeTide.model.TwoFactorOTP;
import com.example.TradeTide.model.User;
import org.springframework.stereotype.Service;

@Service
public interface TwoFactorOtpService {
    TwoFactorOTP createTwoFactorOtp(User user , String otp,String jwt);
    TwoFactorOTP findByUser(int userId);
    TwoFactorOTP findById(String id);
    boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOtp, String otp);
    void deleteTwoFactorOtp(TwoFactorOTP twoFactorOtp);
}
