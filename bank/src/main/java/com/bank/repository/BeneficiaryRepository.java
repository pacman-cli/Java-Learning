package com.bank.repository;

import com.bank.model.Beneficiary;
import com.bank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Beneficiary Repository - Data access layer for Beneficiary entities
 * Provides methods to query beneficiary data
 */
@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {

    /**
     * Find all beneficiaries for a specific user
     * Returns all saved transfer recipients for a user
     * 
     * @param user The user to get beneficiaries for
     * @return List of beneficiaries belonging to this user
     */
    List<Beneficiary> findByUserOrderByNicknameAsc(User user);

    /**
     * Find a beneficiary by user and account
     * Used to check if a beneficiary already exists
     * 
     * @param user The user who owns the beneficiary
     * @param accountId The account ID of the beneficiary
     * @return Optional containing the beneficiary if found
     */
    Optional<Beneficiary> findByUserAndAccountId(User user, Long accountId);

    /**
     * Find a beneficiary by ID and user
     * Ensures user can only access their own beneficiaries
     * 
     * @param id The beneficiary ID
     * @param user The user who owns the beneficiary
     * @return Optional containing the beneficiary if found and owned by user
     */
    Optional<Beneficiary> findByIdAndUser(Long id, User user);
}

