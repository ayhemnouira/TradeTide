package com.example.TradeTide.service;

import com.example.TradeTide.model.PaymentDetails;
import com.example.TradeTide.model.User;

public interface PaymentDetailsService {
    public PaymentDetails addPaymentDetails(String accountNumber,
                                            String accountHolderName, String ifsc,
                                            String bankName, User user) throws Exception;
    public PaymentDetails getPaymentDetailsByUser(User user) throws Exception;
}
