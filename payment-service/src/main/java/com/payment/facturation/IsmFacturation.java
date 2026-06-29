package com.payment.facturation;

import com.payment.domain.ServiceName;
import com.payment.support.DeterministicReferences;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.YearMonth;

@Component
public class IsmFacturation extends AbstractFacturationStrategy {

    public IsmFacturation(DeterministicReferences references) {
        super(references);
    }

    @Override
    public ServiceName service() {
        return ServiceName.ISM;
    }

    @Override
    protected BigDecimal montant() {
        return ServiceName.ISM.montantStandard();
    }

    @Override
    protected String libelle(YearMonth periode) {
        return "Frais de scolarite ISM " + periode;
    }
}
