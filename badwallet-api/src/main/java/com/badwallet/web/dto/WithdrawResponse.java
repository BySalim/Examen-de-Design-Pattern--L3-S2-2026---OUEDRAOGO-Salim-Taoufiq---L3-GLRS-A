package com.badwallet.web.dto;

import java.math.BigDecimal;

public record WithdrawResponse(
        String phoneNumber,
        BigDecimal amount,
        BigDecimal fee,
        BigDecimal totalDebited,
        BigDecimal balanceAfter,
        String currency
) {
}
