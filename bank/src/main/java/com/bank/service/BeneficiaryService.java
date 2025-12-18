package com.bank.service;

import com.bank.model.Account;
import com.bank.model.Beneficiary;
import com.bank.model.User;
import com.bank.repository.AccountRepository;
import com.bank.repository.BeneficiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Beneficiary Service - Business logic layer for beneficiary management
 * Handles adding, retrieving, and managing saved transfer recipients
 */
@Service
@Transactional(readOnly = true)
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final AccountRepository accountRepository;

    /**
     * Constructor injection
     * @param beneficiaryRepository Repository for beneficiary data access
     * @param accountRepository Repository for account data access
     */
    @Autowired
    public BeneficiaryService(BeneficiaryRepository beneficiaryRepository, 
                              AccountRepository accountRepository) {
        this.beneficiaryRepository = beneficiaryRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Add a new beneficiary for a user
     * 
     * @param user The user who wants to add the beneficiary
     * @param accountNumber Account number of the beneficiary
     * @param nickname User-friendly nickname for the beneficiary
     * @return The created beneficiary
     * @throws IllegalArgumentException if account not found or beneficiary already exists
     */
    @Transactional
    public Beneficiary addBeneficiary(User user, String accountNumber, String nickname) {
        // Step 1: Validate nickname is not empty
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new IllegalArgumentException("Nickname cannot be empty");
        }

        // Step 2: Find the beneficiary account
        Account beneficiaryAccount = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountNumber));

        // Step 3: Check if user is trying to add their own account
        if (beneficiaryAccount.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Cannot add your own account as beneficiary");
        }

        // Step 4: Check if beneficiary already exists for this user
        Optional<Beneficiary> existing = beneficiaryRepository.findByUserAndAccountId(user, beneficiaryAccount.getId());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Beneficiary already exists for this account");
        }

        // Step 5: Create and save the beneficiary
        Beneficiary beneficiary = new Beneficiary(user, beneficiaryAccount, nickname);
        return beneficiaryRepository.save(beneficiary);
    }

    /**
     * Get all beneficiaries for a user
     * 
     * @param user The user
     * @return List of beneficiaries for this user
     */
    public List<Beneficiary> getBeneficiaries(User user) {
        return beneficiaryRepository.findByUserOrderByNicknameAsc(user);
    }

    /**
     * Get a beneficiary by ID (ensures it belongs to the user)
     * 
     * @param beneficiaryId The beneficiary ID
     * @param user The user who owns the beneficiary
     * @return Optional containing the beneficiary if found and owned by user
     */
    public Optional<Beneficiary> getBeneficiary(Long beneficiaryId, User user) {
        return beneficiaryRepository.findByIdAndUser(beneficiaryId, user);
    }

    /**
     * Delete a beneficiary
     * 
     * @param beneficiaryId The beneficiary ID
     * @param user The user who owns the beneficiary
     * @throws IllegalArgumentException if beneficiary not found or not owned by user
     */
    @Transactional
    public void deleteBeneficiary(Long beneficiaryId, User user) {
        // Step 1: Find beneficiary and verify ownership
        Beneficiary beneficiary = beneficiaryRepository.findByIdAndUser(beneficiaryId, user)
                .orElseThrow(() -> new IllegalArgumentException("Beneficiary not found or access denied"));

        // Step 2: Delete the beneficiary
        beneficiaryRepository.delete(beneficiary);
    }

    /**
     * Update beneficiary nickname
     * 
     * @param beneficiaryId The beneficiary ID
     * @param user The user who owns the beneficiary
     * @param newNickname New nickname
     * @return Updated beneficiary
     * @throws IllegalArgumentException if beneficiary not found or nickname is empty
     */
    @Transactional
    public Beneficiary updateBeneficiaryNickname(Long beneficiaryId, User user, String newNickname) {
        // Step 1: Validate new nickname
        if (newNickname == null || newNickname.trim().isEmpty()) {
            throw new IllegalArgumentException("Nickname cannot be empty");
        }

        // Step 2: Find beneficiary and verify ownership
        Beneficiary beneficiary = beneficiaryRepository.findByIdAndUser(beneficiaryId, user)
                .orElseThrow(() -> new IllegalArgumentException("Beneficiary not found or access denied"));

        // Step 3: Update nickname
        beneficiary.setNickname(newNickname);
        return beneficiaryRepository.save(beneficiary);
    }
}

