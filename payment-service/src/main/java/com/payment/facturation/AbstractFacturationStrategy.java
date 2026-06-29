package com.payment.facturation;

import com.payment.domain.Facture;
import com.payment.support.DeterministicReferences;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFacturationStrategy implements FacturationStrategy {

    private final DeterministicReferences references;

    protected AbstractFacturationStrategy(DeterministicReferences references) {
        this.references = references;
    }

    @Override
    public final List<Facture> genererFactures(int walletIndex, YearMonth moisCourant) {
        String walletCode = references.walletCode(walletIndex);
        List<Facture> factures = new ArrayList<>();
        int sequence = 1;
        for (int decalageMois : planMensuel()) {
            YearMonth periode = moisCourant.plusMonths(decalageMois);
            factures.add(Facture.builder()
                    .reference(references.factureReference(service(), walletIndex, sequence))
                    .walletCode(walletCode)
                    .serviceName(service())
                    .montant(montant())
                    .periode(periode)
                    .libelle(libelle(periode))
                    .build());
            sequence++;
        }
        return factures;
    }

    protected int[] planMensuel() {
        return new int[] {-1, -1, -1, 0};
    }

    protected abstract BigDecimal montant();

    protected abstract String libelle(YearMonth periode);
}
