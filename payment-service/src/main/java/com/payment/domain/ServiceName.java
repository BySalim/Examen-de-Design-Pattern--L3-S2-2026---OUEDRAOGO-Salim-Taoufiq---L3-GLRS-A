package com.payment.domain;

import java.math.BigDecimal;

public enum ServiceName {

    ISM(new BigDecimal("5000.00")),
    WOYAFAL(new BigDecimal("10000.00"));

    private final BigDecimal montantStandard;

    ServiceName(BigDecimal montantStandard) {
        this.montantStandard = montantStandard;
    }

    public BigDecimal montantStandard() {
        return montantStandard;
    }
}
