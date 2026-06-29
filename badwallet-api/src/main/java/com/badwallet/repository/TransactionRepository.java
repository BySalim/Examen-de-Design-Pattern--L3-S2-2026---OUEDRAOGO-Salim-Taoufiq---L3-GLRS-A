package com.badwallet.repository;

import com.badwallet.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByWalletPhoneNumberOrderByCreatedAtDesc(String phoneNumber);
}
