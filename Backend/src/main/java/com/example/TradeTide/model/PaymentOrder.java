package com.example.TradeTide.model;

import com.example.TradeTide.domain.PaymentOrderStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PaymentOrder {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Long amount;
  private PaymentOrderStatus status;
  @ManyToOne private User user;
}
