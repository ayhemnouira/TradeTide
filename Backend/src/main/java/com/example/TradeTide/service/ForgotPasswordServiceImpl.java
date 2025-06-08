package com.example.TradeTide.service;

import com.example.TradeTide.domain.VerificationType;
import com.example.TradeTide.model.ForgotPasswordToken;
import com.example.TradeTide.model.User;
import com.example.TradeTide.repo.ForgotPasswordRepo;

import java.util.Optional;

public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private  ForgotPasswordRepo forgotPasswordRepo;

    @Override
    public ForgotPasswordToken createToken(User user, String id, String otp, VerificationType verificationType, String sendTo) {
        ForgotPasswordToken token = new ForgotPasswordToken();
        token.setUser(user);
        token.setOtp(otp);
        token.setVerificationType(verificationType);
        token.setSendTo(sendTo);
        token.setId(id);
        return forgotPasswordRepo.save(token);
    }



    @Override
    public ForgotPasswordToken findById(String id) {
        Optional<ForgotPasswordToken> token = forgotPasswordRepo.findById(id);
        return token.orElse(null);
    }

    @Override
    public ForgotPasswordToken findByUserId(int userId) {
        return forgotPasswordRepo.findByUserId(userId);
    }

    @Override
    public void deleteToken(ForgotPasswordToken token) {
        forgotPasswordRepo.delete(token);
    }

}
