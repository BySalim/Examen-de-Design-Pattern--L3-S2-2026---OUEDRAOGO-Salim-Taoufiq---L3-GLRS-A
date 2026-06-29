package com.payment.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PayResult(String walletCode, BigDecimal totalPaye, String devise, List<Ligne> lignes) {

    public record Ligne(String reference, String statut, LocalDate datePaiement, boolean dejaPayee, BigDecimal montant) {
    }
}
