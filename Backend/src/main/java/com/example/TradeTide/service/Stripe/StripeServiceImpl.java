package com.example.TradeTide.service.Stripe;

import com.example.TradeTide.model.PaymentOrder;
import com.example.TradeTide.model.User;
import com.example.TradeTide.repo.PaymentOrderRepo;
import com.example.TradeTide.response.PaymentResponse;
import com.stripe.Stripe;
import com.stripe.model.PaymentMethod;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class StripeServiceImpl implements StripeService {
  @Autowired private PaymentOrderRepo paymentOrderRepo;

  @Value("${STRIPE_API_KEY}")
  private String stripeApiKey;

  @Override
  public PaymentOrder createPaymentOrder(User user, Long amount) throws Exception {
    PaymentOrder paymentOrder = new PaymentOrder();
    paymentOrder.setUser(user);
    paymentOrder.setAmount(amount);
    return paymentOrderRepo.save(paymentOrder);
  }

  @Override
  public PaymentOrder getPaymentOrderById(Long paymentOrderId) throws Exception {
    return paymentOrderRepo
        .findById(paymentOrderId)
        .orElseThrow(() -> new Exception("Payment order not found!"));
  }

  @Override
  public Boolean ProcessPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws Exception {

    return null;
  }

  @Override
  public PaymentResponse createStripePayment(User user, Long amount, Long orderId)
      throws Exception {
    Stripe.apiKey = stripeApiKey;
    SessionCreateParams params =
        SessionCreateParams.builder()
            .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl("https://example.com/success")
            .setCancelUrl("https://example.com/cancel")
            .addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setQuantity(1L)
                    .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("usd")
                            .setUnitAmount(amount * 100)
                            .setProductData(
                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName("Top up wallet")
                                    .build())
                            .build())
                    .build())
            .build();
    Session session = Session.create(params);
    System.out.println("session = " + session);
    PaymentResponse res = new PaymentResponse();
    res.setPayment_url(session.getUrl());
    return res;
  }
}
