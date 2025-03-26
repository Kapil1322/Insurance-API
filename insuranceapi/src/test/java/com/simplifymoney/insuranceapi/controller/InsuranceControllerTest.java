package com.simplifymoney.insuranceapi.controller;

import com.simplifymoney.insuranceapi.model.Insurance;
import com.simplifymoney.insuranceapi.model.PurchaseReceipt;
import com.simplifymoney.insuranceapi.model.UserDetails;
import com.simplifymoney.insuranceapi.service.InsuranceService;
import com.simplifymoney.insuranceapi.service.RecommendationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InsuranceController.class)
@Import({InsuranceService.class, RecommendationService.class}) // Import the actual services
public class InsuranceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InsuranceService insuranceService; // Inject the actual service

    @Autowired
    private RecommendationService recommendationService; // Inject the actual service

    @Test
    void getAllInsurances_shouldReturnOkAndListOfInsurances() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/insurances"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].premium").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].premium").exists());
    }

    @Test
    void purchaseInsurance_shouldReturnCreatedAndReceipt_whenInsuranceFound() throws Exception {
        String insuranceId = "HEALTH001";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/insurances/purchase/" + insuranceId))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.receiptId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.insuranceId").value(insuranceId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.premiumPaid").value(100.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.purchaseDate").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.policyDocumentUrl").exists());
    }

    @Test
    void purchaseInsurance_shouldReturnNotFound_whenInsuranceNotFound() throws Exception {
        String insuranceId = "NON_EXISTENT";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/insurances/purchase/" + insuranceId))
                .andExpect(status().isNotFound());
    }

    @Test
    void downloadPolicy_shouldReturnOkAndPdf_whenPolicyExists() throws Exception {
        String insuranceId = "HEALTH001";

        mockMvc.perform(MockMvcRequestBuilders.get("/api/insurances/policies/" + insuranceId + ".pdf"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(MockMvcResultMatchers.header().string("Content-Disposition", "attachment; filename=policy_" + insuranceId + ".pdf"));
    }

    @Test
    void downloadPolicy_shouldReturnNotFound_whenPolicyDoesNotExist() throws Exception {
        String insuranceId = "NON_EXISTENT";

        mockMvc.perform(MockMvcRequestBuilders.get("/api/insurances/policies/" + insuranceId + ".pdf"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCuratedInsurances_shouldReturnOkAndCuratedList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/insurances/curated")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"age\": 30, \"gender\": \"Male\", \"income\": 60000.00}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].category").exists());
    }

    @Test
    void getRecommendedInsurances_shouldReturnOkAndRecommendedList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/insurances/recommendations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"age\": 35, \"gender\": \"Female\", \"income\": 70000.00}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].premium").exists());
    }
}