package com.payment.facturation;

import com.payment.domain.Facture;
import com.payment.domain.ServiceName;

import java.time.YearMonth;
import java.util.List;

public interface FacturationStrategy {

    ServiceName service();

    List<Facture> genererFactures(int walletIndex, YearMonth moisCourant);
}
