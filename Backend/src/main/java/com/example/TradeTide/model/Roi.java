package com.example.TradeTide.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Roi {
    private double percentage;
    private int times;
}
