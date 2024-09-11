package com.citiustech.ct_claims_processing_system.controller;

import com.citiustech.ct_claims_processing_system.model.Claim;
import com.citiustech.ct_claims_processing_system.service.ClaimProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/claims")
public class ClaimProcessingController {

    @Autowired
    private ClaimProcessingService claimProcessingService;

    @GetMapping("/submit-form")
    public String getClaimSubmissionForm() {
        return "submit-claim-form";  // Return the Thymeleaf template for the form
    }

    @PostMapping("/submit")
    public String submitNewClaim(
            @RequestParam String providerId,
            @RequestParam String memberId,
            @RequestParam String serviceList,
            @RequestParam double totalAmount,
            Model model) {

        Claim claim = claimProcessingService.submitNewClaim(providerId, memberId, serviceList, totalAmount);
        model.addAttribute("claim", claim);
        return "claim-submitted-view"; // Redirect to a Thymeleaf template showing success message
    }

    @GetMapping("/calculate-payment")
    public String calculatePayment(@RequestParam String claimId, Model model) {
        // Call the service to handle the business logic of payment calculation
        String view = claimProcessingService.calculatePayment(claimId, model);
        return view;  // Return the view decided by the service
    }

    @PostMapping("/finalize")
    public String finalizeClaim(@RequestParam String claimId, @RequestParam String decision, @RequestParam(required = false) String denialReasons, Model model) {
        Claim claim = claimProcessingService.finalizeClaim(claimId, decision, denialReasons);
        model.addAttribute("claim", claim);
        return "claims-decision-view";  // Thymeleaf template
    }

    @GetMapping("/eob/{claimId}")
    public String generateEOB(@PathVariable String claimId, Model model) {
        String eob = claimProcessingService.generateEOB(claimId);
        model.addAttribute("eob", eob);
        return "eob-view";  // Thymeleaf template
    }
}
