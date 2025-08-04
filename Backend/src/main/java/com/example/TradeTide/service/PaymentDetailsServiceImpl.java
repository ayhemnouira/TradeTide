package com.example.TradeTide.service;

import com.example.TradeTide.model.PaymentDetails;
import com.example.TradeTide.model.User;
import com.example.TradeTide.repo.PaymentDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentDetailsServiceImpl implements PaymentDetailsService{
    @Autowired
    private PaymentDetailsRepo paymentDetailsRepo;
    @Override
    public PaymentDetails addPaymentDetails(String accountNumber, String accountHolderName, String ifsc, String bankName, User user) throws Exception {
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setAccountNumber(accountNumber);
        paymentDetails.setAccountHolderName(accountHolderName);
        paymentDetails.setIfscCode(ifsc);
        paymentDetails.setBankName(bankName);
        paymentDetails.setUser(user);

        return paymentDetailsRepo.save(paymentDetails);
    }

    @Override
    public PaymentDetails getPaymentDetailsByUser(User user) throws Exception {
        return paymentDetailsRepo.findByUserId(user.getId());
    }
}
