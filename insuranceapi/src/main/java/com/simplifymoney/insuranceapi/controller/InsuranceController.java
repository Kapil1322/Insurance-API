package com.simplifymoney.insuranceapi.controller;

import com.simplifymoney.insuranceapi.model.Insurance;
import com.simplifymoney.insuranceapi.model.PurchaseReceipt;
import com.simplifymoney.insuranceapi.model.UserDetails;
import com.simplifymoney.insuranceapi.service.InsuranceService;
import com.simplifymoney.insuranceapi.service.RecommendationService;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insurances")
public class InsuranceController {

    private final InsuranceService insuranceService;
    private final RecommendationService recommendationService; // For brownie points

    public InsuranceController(InsuranceService insuranceService, RecommendationService recommendationService) {
        this.insuranceService = insuranceService;
        this.recommendationService = recommendationService;
    }

    @GetMapping
    public ResponseEntity<List<Insurance>> getAllInsurances() {
        return ResponseEntity.ok(insuranceService.getAllInsurances());
    }

    @PostMapping("/purchase/{insuranceId}")
    public ResponseEntity<PurchaseReceipt> purchaseInsurance(@PathVariable String insuranceId) {
        try {
            PurchaseReceipt receipt = insuranceService.purchaseInsurance(insuranceId);
            return ResponseEntity.status(HttpStatus.CREATED).body(receipt);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/policies/{insuranceId}.pdf")
    public ResponseEntity<Resource> downloadPolicy(@PathVariable String insuranceId) {
        Resource resource = insuranceService.getPolicyDocument(insuranceId);
        if (resource.exists()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=policy_" + insuranceId + ".pdf");
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Brownie Points: Get curated insurances
    @PostMapping("/curated")
    public ResponseEntity<List<Insurance>> getCuratedInsurances(@RequestBody @Valid UserDetails userDetails) {
        return ResponseEntity.ok(insuranceService.getCuratedInsurances(userDetails));
    }

    // Brownie Points: Get recommended insurances (using RecommendationService)
    @PostMapping("/recommendations")
    public ResponseEntity<List<Insurance>> getRecommendedInsurances(@RequestBody @Valid UserDetails userDetails) {
        return ResponseEntity.ok(recommendationService.getRecommendedInsurances(userDetails));
    }
}