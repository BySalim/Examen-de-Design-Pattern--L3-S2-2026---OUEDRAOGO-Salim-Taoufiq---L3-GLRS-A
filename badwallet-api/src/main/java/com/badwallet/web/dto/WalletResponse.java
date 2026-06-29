package com.badwallet.web.dto;

import com.badwallet.domain.Wallet;

import java.math.BigDecimal;
import java.time.Instant;

public record WalletResponse(
        Long id,
        String phoneNumber,
        String email,
        BigDecimal balance,
        String code,
        String currency,
        Instant createdAt
) {

    public static WalletResponse from(Wallet wallet) {
        return new WalletResponse(
                wallet.getId(),
                wallet.getPhoneNumber(),
                wallet.getEmail(),
                wallet.getBalance(),
                wallet.getCode(),
                wallet.getCurrency(),
                wallet.getCreatedAt()
        );
    }
}
