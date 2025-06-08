package com.example.TradeTide.service;

import com.example.TradeTide.domain.VerificationType;
import com.example.TradeTide.model.User;
import com.example.TradeTide.model.VerificationCode;

public interface VerificationCodeService {
    VerificationCode sendVerificationCode(User user, VerificationType verificationType) ;

    VerificationCode getVerificationCodeById(Long id) throws Exception;

    VerificationCode getVerificationCodeByUserId(int userId) ;


    void deleteVerificationCode(VerificationCode verificationCode) ;
}
