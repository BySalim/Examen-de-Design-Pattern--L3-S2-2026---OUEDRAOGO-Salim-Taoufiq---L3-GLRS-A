package com.badwallet.gateway;

import java.math.BigDecimal;

public record RemoteFacture(
        String reference,
        String walletCode,
        String serviceName,
        BigDecimal montant,
        String devise,
        String statut,
        String periode
) {

    public boolean estImpayee() {
        return "UNPAID".equalsIgnoreCase(statut);
    }
}
