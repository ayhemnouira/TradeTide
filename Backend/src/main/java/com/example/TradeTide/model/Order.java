package com.example.TradeTide.model;

import com.example.TradeTide.domain.OrderStatus;
import com.example.TradeTide.domain.OrderType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
public class Order {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    @ManyToOne
    private User user;

    @Column(nullable = false)
    private OrderType orderType;

    @Column(nullable = false)
    private BigDecimal price;

    private LocalDateTime timestamp = LocalDateTime.now();
    @Column(nullable = false)
    private OrderStatus status ;
    @OneToOne(mappedBy = "order",cascade = CascadeType.ALL)
    private OrderItem orderItem;

}
