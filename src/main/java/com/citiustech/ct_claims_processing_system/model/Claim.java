package com.citiustech.ct_claims_processing_system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "claim")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Claim {

    @Id
    @Column(name = "claim_id", nullable = false)
    private String claimId;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Column(name = "service_list", columnDefinition = "json")
    private String serviceList;

    @Column(name = "fraud_flag", nullable = false)
    private int fraudFlag;

    @Column(name = "claim_status", nullable = false)
    private String claimStatus;

    @Column(name = "payer_cost_share", columnDefinition = "json")
    private String payerCostShare;

    @Column(name = "member_cost_share", columnDefinition = "json")
    private String memberCostShare;

    @Column(name = "eob_details", columnDefinition = "json")
    private String eobDetails;

    @Column(name = "denial_reasons")
    private String denialReasons;

    @Column(name = "total_amount")
    private double totalAmount;
}
