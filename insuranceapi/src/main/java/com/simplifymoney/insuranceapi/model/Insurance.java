package com.simplifymoney.insuranceapi.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Insurance {
    private String id;
    private String name;
    private String description;
    private Double premium;
    private String category; // e.g., Health, Life, Auto
}
