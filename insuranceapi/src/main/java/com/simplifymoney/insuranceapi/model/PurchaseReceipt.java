package com.simplifymoney.insuranceapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseReceipt {
    private String receiptId;
    private String insuranceId;
    private String insuranceName;
    private double premiumPaid;
    private LocalDateTime purchaseDate;
    private String policyDocumentUrl;
}