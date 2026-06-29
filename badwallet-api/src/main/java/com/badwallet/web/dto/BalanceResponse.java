package com.badwallet.web.dto;

import com.badwallet.domain.Wallet;

import java.math.BigDecimal;

public record BalanceResponse(String phoneNumber, BigDecimal balance, String currency) {

    public static BalanceResponse from(Wallet wallet) {
        return new BalanceResponse(wallet.getPhoneNumber(), wallet.getBalance(), wallet.getCurrency());
    }
}
