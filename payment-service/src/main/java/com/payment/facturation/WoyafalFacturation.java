package com.payment.facturation;

import com.payment.domain.ServiceName;
import com.payment.support.DeterministicReferences;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.YearMonth;

@Component
public class WoyafalFacturation extends AbstractFacturationStrategy {

    public WoyafalFacturation(DeterministicReferences references) {
        super(references);
    }

    @Override
    public ServiceName service() {
        return ServiceName.WOYAFAL;
    }

    @Override
    protected BigDecimal montant() {
        return ServiceName.WOYAFAL.montantStandard();
    }

    @Override
    protected String libelle(YearMonth periode) {
        return "Facture electricite WOYAFAL " + periode;
    }
}
