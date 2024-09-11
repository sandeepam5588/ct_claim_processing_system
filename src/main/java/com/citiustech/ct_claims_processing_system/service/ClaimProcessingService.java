package com.citiustech.ct_claims_processing_system.service;

import com.citiustech.ct_claims_processing_system.model.Claim;
import com.citiustech.ct_claims_processing_system.repository.ClaimRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.UUID;

@Service
public class ClaimProcessingService {

    @Autowired
    private ClaimRepository claimRepository;

    public String calculatePayment(String claimId, Model model) {
        // Fetch the claim based on claimId
        Claim claim = claimRepository.findById(claimId).orElse(null);

        if (claim == null) {
            model.addAttribute("message", "Claim not found");
            return "error-view"; // Return an error view if the claim is not found
        }

        // Check claim status
        String claimStatus = claim.getClaimStatus();
        if ("APPROVED".equals(claimStatus)) {
            // Calculate payment if status is APPROVED
            applyPaymentCalculation(claim);

            // Save the updated claim in the database
            claimRepository.save(claim);

            model.addAttribute("claim", claim);
            return "payment-calculation-view"; // Return payment calculation view
        } else if ("DENIED".equals(claimStatus)) {
            // If the claim is DENIED, show a denial message
            model.addAttribute("message", "The claim has been Denied/Rejected");
            return "claim-denied-view"; // Return a denial view
        } else {
            // For any other status, inform the user that payment calculation is not possible
            model.addAttribute("message", "The claim is not yet approved for calculating payment");
            return "claim-not-approved-view"; // Return a view indicating the claim is not ready for payment calculation
        }
    }

    // Business logic for payment calculation
    private void applyPaymentCalculation(Claim claim) {
        double totalAmount = claim.getTotalAmount();
        double memberCostShare = 0.2 * totalAmount; // Example calculation
        double payerResponsibility = totalAmount - memberCostShare;

        claim.setPayerCostShare("{\"payerAmount\":" + payerResponsibility + "}");
        claim.setMemberCostShare("{\"memberAmount\":" + memberCostShare + "}");
        claim.setClaimStatus("PAYMENT_CALCULATED"); // Update the claim status after calculation
    }


    public Claim finalizeClaim(String claimId, String decision, String denialReasons) {
        Claim claim = claimRepository.findById(claimId).orElseThrow(() -> new RuntimeException("Claim not found"));
        claim.setClaimStatus(decision.equals("APPROVED") ? "APPROVED" : "DENIED");
        claim.setDenialReasons(denialReasons);
        claimRepository.save(claim);
        return claim;
    }

    public List<Claim> getAllCalculatedClaims() {
        return claimRepository.findByClaimStatus("PAYMENT_CALCULATED");
    }

    public String generateEOB(String claimId) {
        Claim claim = claimRepository.findById(claimId).orElseThrow(() -> new RuntimeException("Claim not found"));
        // EOB generation logic here
        return claim.getEobDetails();
    }

    public Claim submitNewClaim(String providerId, String memberId, String serviceList, double totalAmount) {
        Claim claim = new Claim();
        claim.setClaimId(UUID.randomUUID().toString());  // Generate a new UUID for the claim ID
        claim.setProviderId(providerId);
        claim.setMemberId(memberId);
        claim.setServiceList(serviceList);
        claim.setTotalAmount(totalAmount);
        claim.setClaimStatus("REQUEST_RECEIVED");  // Set initial status
        claim.setFraudFlag(0);  // Default fraud flag to 0 (not flagged)

        claimRepository.save(claim);
        return claim;
    }
}
