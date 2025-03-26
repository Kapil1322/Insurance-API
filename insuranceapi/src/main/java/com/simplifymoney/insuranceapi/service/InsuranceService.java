package com.simplifymoney.insuranceapi.service;

import com.simplifymoney.insuranceapi.model.Insurance;
import com.simplifymoney.insuranceapi.model.PurchaseReceipt;
import com.simplifymoney.insuranceapi.model.UserDetails;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InsuranceService {

    private final List<Insurance> availableInsurances = new ArrayList<>();
    private final String POLICY_DOCUMENT_PATH = "classpath:policies/dummy_policy.pdf";

    public InsuranceService() {
        // Initialize some dummy insurances
        availableInsurances.add(new Insurance("HEALTH001", "Basic Health Insurance", "Covers basic medical expenses", 100.00, "Health"));
        availableInsurances.add(new Insurance("LIFE002", "Term Life Insurance", "Provides coverage for a specific term", 50.00, "Life"));
        availableInsurances.add(new Insurance("AUTO003", "Comprehensive Auto Insurance", "Covers damages to your vehicle and third-party liability", 150.00, "Auto"));
        availableInsurances.add(new Insurance("HEALTH004", "Premium Health Insurance", "Extensive coverage with additional benefits", 250.00, "Health"));
        availableInsurances.add(new Insurance("LIFE005", "Whole Life Insurance", "Coverage for the entire life with a cash value component", 200.00, "Life"));
    }

    public List<Insurance> getAllInsurances() {
        return availableInsurances;
    }

    public PurchaseReceipt purchaseInsurance(String insuranceId) {
        Insurance insurance = availableInsurances.stream()
                .filter(i -> i.getId().equals(insuranceId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Insurance with ID " + insuranceId + " not found"));

        PurchaseReceipt receipt = new PurchaseReceipt();
        receipt.setReceiptId(UUID.randomUUID().toString());
        receipt.setInsuranceId(insurance.getId());
        receipt.setInsuranceName(insurance.getName());
        receipt.setPremiumPaid(insurance.getPremium());
        receipt.setPurchaseDate(LocalDateTime.now());
        receipt.setPolicyDocumentUrl("/api/insurances/policies/" + insuranceId + ".pdf"); // Endpoint to download policy

        // payment processing.
        // Assuming successful payment.

        return receipt;
    }

    public Resource getPolicyDocument(String insuranceId) {
        // In a real application, you might retrieve a specific policy document
        // based on the purchased insurance. For this example, we'll use a dummy file.
        return new ClassPathResource("policies/dummy_policy.pdf");
    }

    // Brownie Points: Curated insurances based on user details
    public List<Insurance> getCuratedInsurances(UserDetails userDetails) {
        return availableInsurances.stream()
                .filter(insurance -> isInsuranceSuitable(insurance, userDetails))
                .collect(Collectors.toList());
    }

    private boolean isInsuranceSuitable(Insurance insurance, UserDetails userDetails) {
        if (userDetails.getAge() < 18 && insurance.getCategory().equals("Life")) {
            return false; // Example rule: Min age for life insurance
        }
        if (userDetails.getIncome() < 30000 && insurance.getPremium() > 200) {
            return false; // Example rule: Income vs. premium affordability
        }
        if (userDetails.getGender().equalsIgnoreCase("Male") && insurance.getName().contains("Women")) {
            return false; // Example rule: Gender-specific policies (can be expanded)
        }
        return true;
    }
}