package com.example.TradeTide.service.Stripe;

import com.example.TradeTide.model.PaymentOrder;
import com.example.TradeTide.model.User;
import com.example.TradeTide.response.PaymentResponse;
import com.stripe.model.PaymentMethod;

public interface StripeService {
    PaymentOrder createPaymentOrder(User user, Long amount) throws Exception;
    PaymentOrder getPaymentOrderById(Long paymentOrderId) throws Exception;
    Boolean ProcessPaymentOrder(PaymentOrder paymentOrder,String paymentId) throws Exception;
    PaymentResponse createStripePayment(User user, Long amount, Long orderId) throws Exception;
}
