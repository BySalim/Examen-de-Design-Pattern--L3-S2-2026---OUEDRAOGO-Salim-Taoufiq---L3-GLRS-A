package com.payment.service;

import com.payment.domain.Facture;
import com.payment.repository.FactureRepository;
import com.payment.web.dto.PayResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class FacturePaymentService {

    private final FactureRepository factureRepository;
    private final Clock clock;

    public FacturePaymentService(FactureRepository factureRepository, Clock clock) {
        this.factureRepository = factureRepository;
        this.clock = clock;
    }

    @Transactional
    public PayResult payer(String walletCode, List<String> references) {
        List<Facture> factures = factureRepository.findByWalletCodeAndReferenceIn(walletCode, references);
        LocalDate datePaiement = LocalDate.now(clock);

        List<PayResult.Ligne> lignes = new ArrayList<>();
        BigDecimal totalPaye = BigDecimal.ZERO;
        String devise = "XOF";

        for (Facture facture : factures) {
            boolean dejaPayee = facture.estPayee();
            if (!dejaPayee) {
                facture.payer(datePaiement);
                totalPaye = totalPaye.add(facture.getMontant());
            }
            devise = facture.getDevise();
            lignes.add(new PayResult.Ligne(
                    facture.getReference(),
                    facture.getStatut().name(),
                    facture.getDatePaiement(),
                    dejaPayee,
                    facture.getMontant()));
        }

        return new PayResult(walletCode, totalPaye, devise, lignes);
    }
}
