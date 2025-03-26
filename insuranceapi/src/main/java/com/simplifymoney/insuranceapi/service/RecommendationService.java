package com.simplifymoney.insuranceapi.service;

import com.simplifymoney.insuranceapi.model.Insurance;
import com.simplifymoney.insuranceapi.model.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final InsuranceService insuranceService;

    public RecommendationService(InsuranceService insuranceService) {
        this.insuranceService = insuranceService;
    }

    public List<Insurance> getRecommendedInsurances(UserDetails userDetails) {
        List<Insurance> allInsurances = insuranceService.getAllInsurances();

        // Implement more sophisticated recommendation logic here based on user details
        return allInsurances.stream()
                .filter(insurance -> isRecommended(insurance, userDetails))
                .collect(Collectors.toList());
    }

    private boolean isRecommended(Insurance insurance, UserDetails userDetails) {
        // Example recommendation logic:
        if (userDetails.getAge() >= 30 && insurance.getCategory().equals("Life")) {
            return true;
        }
        if (userDetails.getIncome() >= 50000 && insurance.getCategory().equals("Health")) {
            return true;
        }
        return false;
    }
}