package com.example.TradeTide.model;

import jakarta.persistence.*;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;
@Data
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double quantity;
    @ManyToOne
    private Coin coin;
    private double buyPrice;
    private double sellPrice;
    @JsonIgnore
    @OneToOne
    private Order order;
}
