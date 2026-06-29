package com.payment.web.dto;

import com.payment.domain.Facture;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FactureDto(
        String reference,
        String walletCode,
        String serviceName,
        BigDecimal montant,
        String devise,
        String statut,
        String periode,
        LocalDate dateEmission,
        LocalDate datePaiement,
        String libelle
) {

    public static FactureDto from(Facture facture) {
        return new FactureDto(
                facture.getReference(),
                facture.getWalletCode(),
                facture.getServiceName().name(),
                facture.getMontant(),
                facture.getDevise(),
                facture.getStatut().name(),
                facture.getPeriode().toString(),
                facture.getDateEmission(),
                facture.getDatePaiement(),
                facture.getLibelle()
        );
    }
}
