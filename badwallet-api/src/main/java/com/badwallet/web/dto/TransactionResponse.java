package com.badwallet.web.dto;

import com.badwallet.domain.Transaction;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponse(
        Long id,
        String type,
        String direction,
        BigDecimal amount,
        BigDecimal fee,
        BigDecimal balanceAfter,
        String currency,
        String counterpartyPhone,
        String reference,
        String description,
        Instant createdAt
) {

    public static TransactionResponse from(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getType().name(),
                transaction.getDirection().name(),
                transaction.getAmount(),
                transaction.getFee(),
                transaction.getBalanceAfter(),
                transaction.getCurrency(),
                transaction.getCounterpartyPhone(),
                transaction.getReference(),
                transaction.getDescription(),
                transaction.getCreatedAt()
        );
    }
}
