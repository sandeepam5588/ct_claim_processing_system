package com.citiustech.ct_claims_processing_system.repository;

import com.citiustech.ct_claims_processing_system.model.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, String> {
    List<Claim> findByClaimStatus(String status);
}
