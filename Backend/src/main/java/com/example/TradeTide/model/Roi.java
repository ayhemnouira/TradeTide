package com.example.TradeTide.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class Roi implements Serializable {
    private double percentage;
    private int times;
}
