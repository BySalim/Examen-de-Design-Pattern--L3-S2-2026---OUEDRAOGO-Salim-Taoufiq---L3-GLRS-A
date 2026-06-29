package com.badwallet.web.dto;

import java.math.BigDecimal;
import java.util.List;

public record PaymentResponse(
        String phoneNumber,
        String serviceName,
        List<String> paidFactures,
        BigDecimal totalPaid,
        BigDecimal balanceAfter,
        String currency
) {
}
