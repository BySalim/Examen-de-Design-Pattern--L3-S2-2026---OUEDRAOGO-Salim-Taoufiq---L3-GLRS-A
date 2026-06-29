package com.badwallet.web.dto;

import java.math.BigDecimal;

public record TransferResponse(
        String senderPhone,
        String receiverPhone,
        BigDecimal amount,
        BigDecimal senderBalanceAfter,
        BigDecimal receiverBalanceAfter,
        String currency
) {
}
