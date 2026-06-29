package com.badwallet.transaction;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class FeePolicy {

    private static final BigDecimal TAUX = new BigDecimal("0.01");
    private static final BigDecimal PLAFOND = new BigDecimal("5000");

    public BigDecimal fraisRetrait(BigDecimal montant) {
        return montant.multiply(TAUX).min(PLAFOND).setScale(2, RoundingMode.HALF_UP);
    }
}
